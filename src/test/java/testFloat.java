public class testFloat {
    public static void main (String[] args) {

        Float amountOfMoney = 10f;

        float moneyToEUR = toEUR(amountOfMoney);
        System.out.println ("moneyToEUR = "+moneyToEUR);
        moneyToEUR= moneyToEUR+2;
        System.out.println ("moneyToEUR+2 = "+moneyToEUR);


    }

    private static float toEUR (float amountOfMoney) {
        String currency = "EUR";
        Float rateEUR = 26.64f;
        return amountOfMoney*rateEUR;
    }
}
