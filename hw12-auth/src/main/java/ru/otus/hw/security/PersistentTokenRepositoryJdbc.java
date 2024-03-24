package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class PersistentTokenRepositoryJdbc implements PersistentTokenRepository {

    private final JdbcOperations jdbcOperations;

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        var insertSql = "insert into user_tokens (username, series, token, last_used) values(?,?,?,?)";
        jdbcOperations.update(insertSql, token.getUsername(), 
                token.getSeries(), token.getTokenValue(), token.getDate());
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        var updateSql = "update user_tokens set token = ?, last_used = ? where series = ?";
        jdbcOperations.update(updateSql, tokenValue, lastUsed, series);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        var findSql = "select username,series,token,last_used from user_tokens where series = ?";
        try {
            return jdbcOperations.queryForObject(findSql, this::createRememberMeToken, seriesId);
        } catch (EmptyResultDataAccessException ex) {
            this.logger.debug(LogMessage.format("Querying token for series '%s' returned no results.", seriesId), ex);
        } catch (IncorrectResultSizeDataAccessException ex) {
            this.logger.error(LogMessage.format(
                    "Querying token for series '%s' returned more than one value. Series" + " should be unique",
                    seriesId));
        } catch (DataAccessException ex) {
            this.logger.error("Failed to load token for series " + seriesId, ex);
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        var deleteSql = "delete from user_tokens where username = ?";
        jdbcOperations.update(deleteSql, username);
    }
    
    private PersistentRememberMeToken createRememberMeToken(ResultSet rs, int rowNum) throws SQLException {
        return new PersistentRememberMeToken(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4));
    }
}
