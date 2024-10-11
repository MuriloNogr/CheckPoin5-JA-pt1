package br.com.fiap.ja.cp5;

import java.math.BigInteger;
import java.util.Base64;

public class Base64Converter {

    public static BigInteger base64ParaNumerico(String base64) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        return new BigInteger(1, decodedBytes);
    }

    public static void main(String[] args) {
        String base64Cifrado = "V70lDUr7uVNA79SGI/Oddw8+IkqjQV7FyZjfTnWiohn7tegb+wN7o7wkzZaV1bpraHy11SdMnCK0OmR27xC53AhHkCX2xwKJB36YizoF+kZam40hNladuC4vfLv0QCdH5jbvHRENXNGtIXdSZGX28C+sFBXpIyAK0GZxBRHbDFZH/gYD9XhEERHob2bri4TgV9bKab7ZdyzjUVb2d3MlA/zw21QF9zn/vfIyjd/9qsmaFAPtIQmCodbH7lshnWmLVD0QYdhHYcp1FQkvwPQ3SWenTT+QncghxRy2dsSO2m0qy5VdKU+/YCVA9i30OlruXaHiTMVd83gg1Se9N6uxiQ==";

        BigInteger valorNumerico = base64ParaNumerico(base64Cifrado);
        System.out.println("Valor numérico: " + valorNumerico);
    }
}
