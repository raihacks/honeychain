# Honey Chain — Backend Skeleton (Hours 1–4)

Spring Boot 3 / Java 17 / MySQL backend covering:

- **Hours 1–2:** entities (`Beekeeper`, `Hive`, `Batch`, `LedgerRecord`, `SensorReading`) + basic REST endpoints
- **Hours 3–4:** hash-chained ledger — on every `Batch` status change, a new `LedgerRecord` is appended with `currentHash = SHA-256(previousHash + json(payload))`, plus a `/verify` endpoint that recomputes the chain and flags tampering

## Project layout

```
src/main/java/com/honeychain/
  entity/       Beekeeper, Hive, Batch, BatchStatus, LedgerRecord, SensorReading
  repository/   Spring Data JPA repositories
  dto/          request/response payloads + LedgerPayload (the thing that gets hashed)
  service/      BeekeeperService, HiveService, SensorReadingService, BatchService (the ledger logic), HashingService
  controller/   REST controllers
  config/       GlobalExceptionHandler
```

## Run it

1. Create a MySQL database (or let it auto-create — see `application.properties`, which points at `jdbc:mysql://localhost:3306/honeychain`). Update the username/password there.
2. `mvn spring-boot:run`
3. Server starts on `http://localhost:8080`

## The ledger logic, in plain terms

- `Batch.status` moves strictly forward through `CREATED → HARVESTED → QUALITY_CHECKED → PACKAGED`. `BatchService.transitionStatus` rejects skips or backwards moves.
- Every transition (including the initial `CREATED` on creation) calls `appendLedgerRecord`, which:
  1. builds a `LedgerPayload` (batch id/code, hive code, status, quantity, sequence number, timestamp)
  2. serializes it to a JSON string via Jackson (field order pinned with `@JsonPropertyOrder` for determinism)
  3. takes `previousHash` = the batch's `latestHash` (or 64 zero-chars as the genesis hash for record #1)
  4. computes `currentHash = SHA-256(previousHash + payloadJson)`
  5. saves the `LedgerRecord` (storing the *exact* `payloadJson` string, not just the object — verification re-hashes this stored string, so it doesn't depend on re-serializing identically later)
  6. updates `Batch.latestHash` so the next transition chains off it
- `GET /api/batches/{id}/verify` walks the batch's records in sequence and checks three things per record: sequence numbers are contiguous, each `previousHash` matches the prior record's `currentHash` (or genesis for record #1), and recomputing `SHA-256(previousHash + payloadJson)` still equals the stored `currentHash`. Any mismatch is reported as a specific issue and flips `valid` to `false`.

## Example flow (curl)

```bash
# 1. Register a beekeeper
curl -X POST localhost:8080/api/beekeepers -H "Content-Type: application/json" -d '{
  "name": "Ramesh Kumar", "phone": "9876543210", "village": "Sonipat", "cluster": "KVIC-North-1"
}'
# -> { "id": 1, ... }

# 2. Register a hive
curl -X POST localhost:8080/api/hives -H "Content-Type: application/json" -d '{
  "hiveCode": "HIVE-001", "beekeeperId": 1, "latitude": 28.99, "longitude": 77.01
}'
# -> { "id": 1, ... }

# 3. Create a batch (this writes ledger record #1, status=CREATED)
curl -X POST localhost:8080/api/batches -H "Content-Type: application/json" -d '{
  "batchCode": "BATCH-2026-001", "hiveId": 1, "quantityKg": 12.5
}'
# -> { "id": 1, "status": "CREATED", "latestHash": "..." }

# 4. Add a sensor reading
curl -X POST localhost:8080/api/sensor-readings -H "Content-Type: application/json" -d '{
  "hiveId": 1, "temperatureC": 34.2, "humidityPct": 55.0, "weightKg": 41.3
}'

# 5. Move the batch through its lifecycle (each call appends a ledger record)
curl -X PATCH localhost:8080/api/batches/1/status -H "Content-Type: application/json" -d '{"newStatus": "HARVESTED"}'
curl -X PATCH localhost:8080/api/batches/1/status -H "Content-Type: application/json" -d '{"newStatus": "QUALITY_CHECKED"}'
curl -X PATCH localhost:8080/api/batches/1/status -H "Content-Type: application/json" -d '{"newStatus": "PACKAGED"}'

# Trying to skip a stage or go backwards -> 409 Conflict
curl -X PATCH localhost:8080/api/batches/1/status -H "Content-Type: application/json" -d '{"newStatus": "CREATED"}'

# 6. Verify the chain
curl localhost:8080/api/batches/1/verify
# -> { "valid": true, "batchId": 1, "recordsChecked": 4, "issues": [] }
```

## Note on the build environment

This was written and reviewed by hand (no local Maven/internet access to fetch dependencies in this sandbox to actually run `mvn compile`), so give it a real build on your machine before demo day — it's straightforward Spring Boot/JPA, but double-check dependency versions in `pom.xml` resolve cleanly with your local Maven setup.

## QR code + consumer verification page

- `GET /api/batches/{id}/qr?size=300` — returns a PNG QR code encoding `{app.base-url}/verify/{id}`. Set `app.base-url` in `application.properties` to your real domain before printing labels (defaults to `http://localhost:8080` for local testing).
- `GET /api/batches/{id}/qr-url` — plain-text version of that URL, handy for debugging without decoding the image.
- `GET /verify/{id}` — the page a consumer lands on after scanning. Server-rendered with Thymeleaf (`src/main/resources/templates/verify.html`), so it works with zero JS and no separate frontend deploy. Shows:
  - Beekeeper name + village
  - Hive location (lat/lon)
  - Harvest date (pulled from the `HARVESTED` ledger record)
  - Hive health status at the most recent sensor reading (`Healthy` / `Needs Attention` / `No sensor data yet`, computed from simple temperature/humidity thresholds — not a real diagnostic model, just enough until the AI health service exists)
  - The full chain-of-custody hash trail, plus a verified/failed badge driven by `BatchService.verifyChain(...)` — so a tampered record is visibly flagged on the public page, not just in an internal API
  - If the batch code doesn't exist, a plain "we couldn't find this batch" page instead of an error page

`ConsumerVerificationService` assembles all of this into one `ConsumerVerificationView` so the controller stays thin — same batch data the internal APIs use, just reshaped for a public audience (no internal IDs beyond what's needed, no raw payload JSON, only the hash itself).

### Try it

```bash
# After creating a batch and moving it through some transitions (see above):
curl localhost:8080/api/batches/1/qr-url
# -> http://localhost:8080/verify/1

curl -o batch1-qr.png localhost:8080/api/batches/1/qr
# open batch1-qr.png, or just visit http://localhost:8080/verify/1 in a browser
```

## Sample data

`config/DataSeeder.java` runs on startup and seeds realistic sample data through the real services (not raw SQL) — so every seeded batch gets a properly computed hash chain, same as if it came from real API calls:

- 2 beekeepers (Ramesh Kumar / Sonipat, Sunita Devi / Panipat), 3 hives
- 4 sensor readings (one deliberately outside the healthy range, so you can see "Needs Attention" on the verify page)
- 3 batches in different lifecycle states: `BATCH-2026-001` fully `PACKAGED`, `BATCH-2026-002` at `QUALITY_CHECKED`, `BATCH-2026-101` still `CREATED`

It only seeds if the `batch` table is empty, so it's safe to leave in and won't duplicate data on restart. Console output on startup tells you the seeded batch ID to try — e.g. `GET /api/batches/1/verify` or open `http://localhost:8080/verify/1` in a browser to see the full consumer page with real data already in it.

## Not yet wired up (next hours, per your plan)

- Syncing the verification read path from a cache instead of hitting the primary DB directly (per your modular architecture decision)
- IoT ingestion service, AI inference service
- Cluster/tenant model (Cluster → Beekeepers → Hives)
- Public anchoring of periodic checkpoint hashes
