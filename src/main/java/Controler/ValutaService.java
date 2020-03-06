package Controler;

public class ValutaService{
    //хардкод обменник

    private final Long idUAH = (long)1;
    private final Long idUSD = (long)3;
    private final Long idEUR = (long)2;

    private final float toBuyUSD = 25;
    private final float toSellUSD = 24;

    private final float toBuyEUR = 27;
    private final float toSellEUR = 26;


    public Float changeValuta (Long idValutaFrom, Long idValutaTo, Float amountOfMoney){
        //валюта 1 - "UAH"
        if(idValutaFrom.equals (idValutaTo)) {
            return amountOfMoney;
        }
        if(idValutaFrom.equals (idUAH)){
           if (idValutaTo.equals (idUSD)){
                return UAHtoUSD (amountOfMoney);
            } else if (idValutaTo.equals (idEUR)){
                return UAHtoEUR (amountOfMoney);
            }

        }

        if(idValutaFrom.equals (idEUR)){
            if (idValutaTo.equals (idUAH)){
                return EURtoUAH (amountOfMoney);
            } else if (idValutaTo.equals (idUSD)){
                Float result = EURtoUAH (amountOfMoney);
                result = UAHtoUSD (result);
                return result;
            }
        }

        if(idValutaFrom.equals (idUSD)){
            if (idValutaTo.equals (idUAH)){
                return USDtoUAH (amountOfMoney);
            } else if (idValutaTo.equals (idEUR)){
                Float result = USDtoUAH (amountOfMoney);
                result = UAHtoEUR (result);
                return result;
            }
        }



        return null;
    }

    public Float UAHtoUSD ( Float amountOfMoney){
        return amountOfMoney/toBuyUSD;
    }

    public Float USDtoUAH ( Float amountOfMoney){
        return amountOfMoney*toSellUSD;
    }

    public Float UAHtoEUR ( Float amountOfMoney){
        return amountOfMoney/toBuyEUR;
    }

    public Float EURtoUAH ( Float amountOfMoney){
        return amountOfMoney*toSellEUR;
    }

}