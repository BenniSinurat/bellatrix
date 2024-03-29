package org.bellatrix.data;

public abstract class StatusBuilder {

	public static ResponseStatus getStatus(String arg) {

		return StatusBuilder.getStatus(Status.valueOf(arg));
	}

	public static ResponseStatus getStatus(Status arg) {
		switch (arg) {
		case PROCESSED:
			return new ResponseStatus("A00", "PROCESSED", "Transaction completed");
		case REQUEST_RECEIVED:
			return new ResponseStatus("A02", "REQUEST_RECEIVED", "Your Transaction has been received for processing");
		case UNAUTHORIZED_ACCESS:
			return new ResponseStatus("A90", "UNAUTHORIZED_ACCESS",
					"We cannot authorized you, please check your authorization");
		case SERVICE_NOT_ALLOWED:
			return new ResponseStatus("A16", "SERVICE_NOT_ALLOWED", "Group not allowed to access Webservices");
		case ACCESS_DENIED:
			return new ResponseStatus("A97", "ACCESS_DENIED", "Please contact administrator");
		case INVALID:
			return new ResponseStatus("A14", "INVALID", "Invalid credential");
		case CREDENTIAL_INVALID:
			return new ResponseStatus("A16", "CREDENTIAL_INVALID", "Please contact administrator");
		case VALID:
			return new ResponseStatus("A01", "VALID", "Credential Valid");
		case BLOCKED:
			return new ResponseStatus("A16", "BLOCKED", "Credential blocked");
		case PARENT_ID_NOT_FOUND:
			return new ResponseStatus("M44", "PARENT_ID_NOT_FOUND", "Parent ID for this member not found on system");
		case MEMBER_NOT_FOUND:
			return new ResponseStatus("M14", "MEMBER_NOT_FOUND", "Member not found on system");
		case MEMBER_ALREADY_REGISTERED:
			return new ResponseStatus("M16", "MEMBER_ALREADY_REGISTERED", "Member already registered on system");
		case DESTINATION_MEMBER_NOT_FOUND:
			return new ResponseStatus("M15", "DESTINATION_MEMBER_NOT_FOUND", "Destination member not found on system");
		case INVALID_ADDRESS:
			return new ResponseStatus("M01", "INVALID_ADDRESS", "Special character found in address");
		case INVALID_NAME:
			return new ResponseStatus("M02", "INVALID_NAME", "Special character or numberic found in name");
		case NO_TRANSACTION:
			return new ResponseStatus("S84", "NO_TRANSACTION", "No transaction found for the specified account");
		case INVALID_ACCOUNT:
			return new ResponseStatus("S24", "INVALID_ACCOUNT", "Invalid source account/permission not allowed");
		case ACCOUNT_NOT_FOUND:
			return new ResponseStatus("S14", "ACCOUNT_NOT_FOUND", "Specified account not found");
		case INVALID_FEE_ACCOUNT:
			return new ResponseStatus("S34", "INVALID_FEE_ACCOUNT",
					"Invalid fee source account/permission not allowed");
		case INVALID_DESTINATION_ACCOUNT:
			return new ResponseStatus("S15", "INVALID_DESTINATION_ACCOUNT",
					"Invalid destination account/permission not allowed");
		case INVALID_FEE_DESTINATION_ACCOUNT:
			return new ResponseStatus("S25", "INVALID_FEE_DESTINATION_ACCOUNT",
					"Invalid fee destination account/permission not allowed");
		case INSUFFICIENT_BALANCE:
			return new ResponseStatus("S22", "INSUFFICIENT_BALANCE",
					"You dont have enough balance to process this transaction");
		case CREDIT_LIMIT_REACHED:
			return new ResponseStatus("S40", "CREDIT_LIMIT_REACHED", "Your account limit has reached");
		case MONTHLY_CREDIT_LIMIT_REACHED:
			return new ResponseStatus("S42", "MONTHLY_CREDIT_LIMIT_REACHED", "Your monthly account limit has reached");
		case DESTINATION_CREDIT_LIMIT_REACHED:
			return new ResponseStatus("S41", "DESTINATION_CREDIT_LIMIT_REACHED",
					"The destination account limit has reached");
		case MONTHLY_DESTINATION_CREDIT_LIMIT_REACHED:
			return new ResponseStatus("S43", "MONTHLY_DESTINATION_CREDIT_LIMIT_REACHED",
					"The destination monthly account limit has reached");
		case BALANCE_LIMIT_REACHED:
			return new ResponseStatus("S44", "BALANCE_LIMIT_REACHED", "You have reached your max balance limit");
		case INVALID_TRANSFER_TYPE:
			return new ResponseStatus("T14", "INVALID_TRANSFER_TYPE", "Invalid transfer type/permission not allowed");
		case TRANSACTION_AMOUNT_BELOW_LIMIT:
			return new ResponseStatus("T16", "TRANSACTION_AMOUNT_BELOW_LIMIT",
					"Transaction amount is below the threshold limit");
		case TRANSACTION_AMOUNT_ABOVE_LIMIT:
			return new ResponseStatus("T18", "TRANSACTION_AMOUNT_ABOVE_LIMIT",
					"Transaction amount is above the threshold limit");
		case TRANSACTION_BLOCKED:
			return new ResponseStatus("T40", "TRANSACTION_BLOCKED",
					"Transaction is blocked, please contact administrator");
		case TRANSACTION_LIMIT_REACHED:
			return new ResponseStatus("T44", "TRANSACTION_LIMIT_REACHED",
					"You have reached your transaction limit, please contact administrator");
		case DUPLICATE_TRANSACTION:
			return new ResponseStatus("P16", "DUPLICATE_TRANSACTION", "Duplicate transaction entry");
		case INVALID_PARAMETER:
			return new ResponseStatus("P14", "INVALID_PARAMETER", "Invalid request parameter");
		case NEGATIVE_AMOUNT:
			return new ResponseStatus("P27", "NEGATIVE_AMOUNT", "Negative total transaction amount");
		case INVALID_GROUP:
			return new ResponseStatus("G14", "INVALID_GROUP", "Member Group not found");
		case OTP_VALIDATION_FAILED:
			return new ResponseStatus("T64", "OTP_VALIDATION_FAILED", "Wrong OTP, please retry");
		case OTP_EXPIRED:
			return new ResponseStatus("T65", "OTP_EXPIRED", "OTP already expired, please retry");
		case OTP_FAILED:
			return new ResponseStatus("T66", "OTP_FAILED", "OTP notification failed, please retry");
		case PAYMENT_NOT_FOUND:
			return new ResponseStatus("T82", "PAYMENT_NOT_FOUND", "The specified Payment not found");
		case BANK_NOT_FOUND:
			return new ResponseStatus("I14", "BANK_NOT_FOUND", "The specified Bank not found");
		case BANK_ACCOUNT_NOT_FOUND:
			return new ResponseStatus("I14", "BANK_ACCOUNT_NOT_FOUND", "The specified Bank Account not found");
		case BANK_ACCOUNT_LIMIT:
			return new ResponseStatus("I15", "BANK_ACCOUNT_LIMIT", "You have reached your bank account");
		case BANK_ACCOUNT_ALREADY_REGISTERED:
			return new ResponseStatus("I16", "BANK_ACCOUNT_ALREADY_REGISTERED", "Bank Account already registered");
		case PAYMENT_CODE_NOT_FOUND:
			return new ResponseStatus("V14", "PAYMENT_CODE_NOT_FOUND",
					"The specified Payment Code already expired or not found");
		case TERMINAL_NOT_FOUND:
			return new ResponseStatus("E14", "TERMINAL_NOT_FOUND", "The specified POS Terminal not found");
		case INVALID_AMOUNT:
			return new ResponseStatus("V04", "INVALID_AMOUNT", "Invalid transaction amount");
		case BILLER_UNAVAILABLE:
			return new ResponseStatus("B14", "BILLER_UNAVAILABLE", "No Biller available for specified member");
		case INVALID_CURRENCY:
			return new ResponseStatus("S24", "INVALID_CURRENCY", "Invalid currency");
		case EMAIL_NOT_VERIFY:
			return new ResponseStatus("E01", "EMAIL_NOT_VERIFY", "Please verify your email");
		case KYC_REJECTED:
			return new ResponseStatus("K01", "KYC_REJECTED",
					"Upgrade account has been rejected, please verify your data");
		case KYC_PENDING:
			return new ResponseStatus("K02", "KYC_PENDING", "Upgrade account in process");
		case TRANSACTION_COMPLETED:
			return new ResponseStatus("T00", "TRANSACTION_COMPLETED", "Billing transaction complete");
		case INVALID_PAYMENT_CHANNEL:
			return new ResponseStatus("C01", "INVALID_PAYMENT_CHANNEL",
					"Invalid payment channel/permission not allowed");
		case BILLING_INACTIVE:
			return new ResponseStatus("B15", "BILLING_INACTIVE", "Billing inactive");
		case SCHEDULED_TRANSFER_NOT_FOUND:
			return new ResponseStatus("SH14", "SCHEDULED_TRANSFER_NOT_FOUND", "The specified schedule transfer not found");
		default:
			return new ResponseStatus("E99", "UNKNOWN_ERROR", "Unknown Error");
		}
	}
}
