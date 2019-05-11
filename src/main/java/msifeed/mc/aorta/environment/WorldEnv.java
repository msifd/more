package msifeed.mc.aorta.environment;

public class WorldEnv {

    public boolean snow = false;
    public boolean stackSnow = false;
    public boolean meltSnow = false;
    public Time time = new Time();
    public Rain rain = new Rain();

    void load(WorldEnvMapData data) {
        rain.accumulated = data.rainAccumulated;
    }

    public static class Time {
        public String mode = "vanilla";
        public int offsetHours = 0;
        public double scale = 1;
        public long fixedTime = 0;
    }

    public static class Rain {
        public int income = 0;
        public int outcome = 0;
        public int minThreshold = 0;
        public int maxThreshold = 0;
        public int thunderThreshold = 10;
        public int rainfallDice = 100;

        public transient int accumulated = 0;
    }
}