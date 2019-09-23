import java.util.LinkedHashMap;

public class Global {

    static String[] basicVars = {
            "team name",
            "hab lvl 2", "auto hatch", "auto cargo",
            "auto h1", "auto h2", "auto h3", "auto c1", "auto c2", "auto c3",
            "tele hatch", "tele cargo",
            "tele h1", "tele h2", "tele h3", "tele c1", "tele c2", "tele c3",
            "HAB climb", "driver ability", "comments"
    };

    static String[] quantVars = {
            "hab lvl 2", "auto hatch", "auto cargo",
            "auto h1", "auto h2", "auto h3", "auto c1", "auto c2", "auto c3",
            "tele hatch", "tele cargo",
            "tele h1", "tele h2", "tele h3", "tele c1", "tele c2", "tele c3",
            "HAB climb", "driver ability"
    };

    static double[] firstPick={
            3,2,2,
            1.5,1.5,1.5,2,2,2,
            2,2,
            2,2,2,2,2,2,
            1,0
    };


    static double[] hatchTotal={
            0,1,0,
            1,1,1,0,0,0,
            1,0,
            1,1,1,0,0,0,
            0,0
    };

    static double[] cargoShip={
            0,0,0,
            0,0,0,1,1,1,
            0,0,
            0,0,0,1,1,1,
            0,0
    };
    static double[] cargoTotal={
            0,0,1,
            0,0,0,1,1,1,
            0,1,
            0,0,0,1,1,1,
            0,0
    };
    static double[] hatchShip={
            0,0,0,
            1,1,1,0,0,0,
            0,0,
            1,1,1,0,0,0,
            0,0
    };
    static double[] shipPieces={
            0,0,0,
            1,1,1,1,1,1,
            0,0,
            1,1,1,1,1,1,
            0,0
    };
    static double[] totalPieces={
            0,1,1,
            1,1,1,1,1,1,
            1,1,
            1,1,1,1,1,1,
            0,0
    };

    static double[] autoCargo={
            0,0,1,
            0,0,0,1,1,1,
            0,0,
            0,0,0,0,0,0,
            0,0
    };
    static double[] autoHatch={
            0,0,1,
            0,0,0,1,1,1,
            0,0,
            0,0,0,0,0,0,
            0,0
    };
    static double[] autocracy={
            1.5,1,1,
            1,1,1,1,1,1,
            0,0,
            0,0,0,0,0,0,
            0,0
    };
    static LinkedHashMap<String, double[]> dict(){//make sure these vars have different name from quant vars
        LinkedHashMap<String, double[]> map= new LinkedHashMap<String, double[]>();
        map.put("first pick", firstPick);
        map.put("auto hatches", autoHatch);
        map.put("auto cargos", autoCargo);
        map.put("autocracy", autocracy);;
        map.put("total hatches", hatchTotal);
        map.put("total cargo", cargoTotal);
        map.put("ship hatches", hatchShip);
        map.put("ship cargo", cargoShip);
        map.put("ship pieces", shipPieces);
        map.put("total pieces", totalPieces);

        return map;
    }



}
