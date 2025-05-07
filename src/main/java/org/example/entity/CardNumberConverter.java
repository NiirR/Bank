package org.example.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CardNumberConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return EncryptionUtil.encode(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        String decrypted = EncryptionUtil.decode(dbData);
        return maskCardNumber(decrypted);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 12) {
            return cardNumber;
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}
