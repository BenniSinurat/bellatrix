package org.bellatrix.process;

import org.bellatrix.data.Currencies;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.bellatrix.services.CurrencyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyValidation {

	@Autowired
	private BaseRepository baseRepository;

	public Currencies validateCurrencyByID(Integer id) throws TransactionException {
		Currencies cr = baseRepository.getCurrenciesRepository().loadCurrencyByID(id);
		if (cr == null) {
			throw new TransactionException(String.valueOf(Status.INVALID_CURRENCY));
		}
		return cr;
	}

	public void createCurrency(CurrencyRequest req) {
		baseRepository.getCurrenciesRepository().createCurrency(req.getName(), req.getCode(), req.getPrefix(),
				req.getTrailer(), req.getFormat(), req.getGroupingSeparator(), req.getDecimalSeparator());
	}

	public void updateCurrency(CurrencyRequest req) {
		baseRepository.getCurrenciesRepository().updateCurrency(req.getName(), req.getCode(), req.getPrefix(),
				req.getTrailer(), req.getFormat(), req.getGroupingSeparator(), req.getDecimalSeparator(), req.getId());
	}
}