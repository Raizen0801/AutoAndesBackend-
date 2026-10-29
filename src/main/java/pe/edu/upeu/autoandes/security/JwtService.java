package pe.edu.upeu.autoandes.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${app.jwt.secret:w9Z6dP/dzykNH/uiI3Z/KGjBlP0H4Ji+Tuq4fm2cTNuLD4jWafu1gBL74V2SJEvy}")
    private String jwtSecret;

    @Value("${app.jwt.expiracion-minutos:60}")
    private long jwtExpirationMinutes;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarToken(UsuarioPrincipal usuarioPrincipal) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", usuarioPrincipal.getId());
        extraClaims.put("nombreCompleto", usuarioPrincipal.getNombreCompleto());
        extraClaims.put("roles", usuarioPrincipal.getRoles());
        extraClaims.put("permisos", usuarioPrincipal.getPermisos());

        long expiracionMilis = jwtExpirationMinutes * 60 * 1000;
        Date ahora = new Date();
        Date fechaExpiracion = new Date(ahora.getTime() + expiracionMilis);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(usuarioPrincipal.getUsername())
                .issuedAt(ahora)
                .expiration(fechaExpiracion)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean esTokenValido(String token, String username) {
        try {
            final String usernameToken = extraerUsername(token);
            return (usernameToken.equals(username) && !esTokenExpirado(token));
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token JWT inválido o alterado: {}", e.getMessage());
            return false;
        }
    }

    public boolean esTokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    private Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
