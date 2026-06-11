package org.claumann.travelagency.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class TokenService {

    private static final String SECRET = "minha-chave-super-secreta-com-pelo-menos-32-caracteres";
    private static final long EXPIRATION_IN_MILLIS = 60 * 60 * 1000; // 1 hora

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public TokenService() {
        SecretKey secretKey = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
        this.decoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    public String generateToken(final UserDetails user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(EXPIRATION_IN_MILLIS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public String validateToken(final String token) {
        Jwt jwt = decoder.decode(token);
        return jwt.getSubject();
    }

}
