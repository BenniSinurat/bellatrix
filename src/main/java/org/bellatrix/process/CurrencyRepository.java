package org.bellatrix.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.bellatrix.data.Currencies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class CurrencyRepository {

	private JdbcTemplate jdbcTemplate;

	public List<Currencies> loadAllCurrencies() {
		try {
			List<Currencies> currency = this.jdbcTemplate.query("select * from currency", new Object[] {},
					new RowMapper<Currencies>() {
						public Currencies mapRow(ResultSet rs, int rowNum) throws SQLException {
							Currencies currency = new Currencies();
							currency.setId(rs.getInt("id"));
							currency.setName(rs.getString("name"));
							currency.setCode(rs.getString("code"));
							currency.setDecimal(rs.getString("decimal_separator"));
							currency.setGrouping(rs.getString("grouping_separator"));
							currency.setFormat(rs.getString("format"));
							currency.setPrefix(rs.getString("prefix"));
							currency.setTrailer(rs.getString("trailer"));
							return currency;
						}
					});
			return currency;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Currencies loadCurrencyByID(Integer currencyID) {
		try {
			Currencies currency = this.jdbcTemplate.queryForObject("select * from currency where id = ?",
					new Object[] { currencyID }, new RowMapper<Currencies>() {
						public Currencies mapRow(ResultSet rs, int rowNum) throws SQLException {
							Currencies currency = new Currencies();
							currency.setId(rs.getInt("id"));
							currency.setName(rs.getString("name"));
							currency.setCode(rs.getString("code"));
							currency.setDecimal(rs.getString("decimal_separator"));
							currency.setGrouping(rs.getString("grouping_separator"));
							currency.setFormat(rs.getString("format"));
							currency.setPrefix(rs.getString("prefix"));
							currency.setTrailer(rs.getString("trailer"));
							return currency;
						}
					});
			return currency;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Currencies loadCurrencyByAccountID(Integer accountID) {
		try {
			Currencies currency = this.jdbcTemplate.queryForObject(
					"select a.id, a.name, a.code, a.prefix, a.trailer, a.format, a.grouping_separator, a.decimal_separator from currency a inner join accounts b on b.currency_id = a.id where b.id=?;",
					new Object[] { accountID }, new RowMapper<Currencies>() {
						public Currencies mapRow(ResultSet rs, int rowNum) throws SQLException {
							Currencies currency = new Currencies();
							currency.setId(rs.getInt("id"));
							currency.setName(rs.getString("name"));
							currency.setCode(rs.getString("code"));
							currency.setDecimal(rs.getString("decimal_separator"));
							currency.setGrouping(rs.getString("grouping_separator"));
							currency.setFormat(rs.getString("format"));
							currency.setPrefix(rs.getString("prefix"));
							currency.setTrailer(rs.getString("trailer"));
							return currency;
						}
					});
			return currency;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void createCurrency(String name, String code, String prefix, String trailer, String format, String grouping,
			String decimal) {
		jdbcTemplate.update(
				"insert into currency (name, code, prefix, trailer, format, grouping_separator, decimal_separator) values (?, ?, ?, ?, ?, ?, ?)",
				name, code, prefix, trailer, format, grouping, decimal);
	}

	public void updateCurrency(String name, String code, String prefix, String trailer, String format, String grouping,
			String decimal, Integer id) {
		this.jdbcTemplate.update(
				"update  currency set name = ?, code = ?, prefix = ?, trailer = ?, format = ?, grouping_separator = ?, decimal_separator = ? where id = ?",
				new Object[] { name, code, prefix, trailer, format, grouping, decimal, id });
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
