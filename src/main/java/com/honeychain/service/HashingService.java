package com.honeychain.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Pure SHA-256 hashing helper used by the ledger's hash chain.
 *
 * currentHash = SHA-256(previousHash + payloadJson)
 *
 * The two strings are concatenated (not hashed separately) so that a
 * tamper with either the previous hash pointer or the payload changes
 * the resulting hash.
 */
@Service
public class HashingService {

    /** 64 zero characters – the "previous hash" of the first record in any chain. */
    public static final String GENESIS_HASH = "0".repeat(64);

    public String sha256(String previousHash, String payloadJson) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(
                    (previousHash + payloadJson).getBytes(StandardCharsets.UTF_8)
            );
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to be available on every standard JVM.
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
