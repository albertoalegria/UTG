package com.alegria.utg.utilities;

/**
 * @author Alberto Alegria
 */
public class Constants {
    public static final int TIMES_COUNT = 70;

    public static class DB {
        public static final String CLASS_NAME = "com.mysql.jdbc.Driver";
        public static final String DATABASE_NAME = "time_data";
        public static final String DATABASE_URL = "jdbc:mysql://localhost:8889/" + DATABASE_NAME + "?user=root&password=12345";

        public static class Teacher {
            public static final String TB_NAME = "teacher";
            public static final String COL_NAME = "name";
            public static final String COL_HOURS = "hours";
            public static final String COL_COURSES = "courses";
            public static final String COL_TIMES = "times";
            public static final String COL_FTIME = "first_time";
            public static final String COL_LTIME = "last_time";

            public static final String[] DB_COLS = {COL_NAME, COL_HOURS, COL_COURSES, COL_TIMES, COL_FTIME, COL_LTIME};
        }

        public static class Classroom {
            public static final String TB_NAME = "classroom";
            public static final String COL_NAME = "name";
            public static final String COL_SIZE = "size";
            public static final String COL_CAREERS = "careers";
        }

        public static class Course {
            public static final String TB_NAME = "course";
            public static final String COL_NAME = "name";
            public static final String COL_GROUP = "group";
            public static final String COL_SIZE = "size";
            public static final String COL_HOURS = "hours";
            public static final String COL_CAREER = "career";
            public static final String COL_SHIFT = "shift";
            public static final String COL_TYPE = "type";
            public static final String[] DB_COLS = {COL_NAME, COL_GROUP, COL_SIZE, COL_HOURS, COL_CAREER, COL_SHIFT, COL_TYPE};

            ///TODO Fix courses and classroom

        }

        public static final String TEACHERS_DB_NAME = "teachers";
        public static final String CLASSROOMS_DB_NAME = "classrooms";
        public static final String COURSES_DB_NAME = "courses";

        public static final String[] TEACHERS_DB_COLS = {"id", "name", "hours", "courses", "times"};
        public static final String[] COURSES_DB_COLS = {"id", "name", "shift", "size", "hours", "career"};
        public static final String[] CLASSROOMS_DB_COLS = {"id", "name", "size", "careers"};
    }

    public static class Day {
        public static final String MONDAY = "Monday";
        public static final String TUESDAY = "Tuesday";
        public static final String WEDNESDAY = "Wednesday";
        public static final String THURSDAY = "Thursday";
        public static final String FRIDAY = "Friday";
    }

    public static class Shift {
        public static final int MORNING = 0x01;
        public static final int AFTERNOON = 0x02;

        public static class Names {
            public static final String MORNING = "Morning";
            public static final String AFTERNOON = "Afternoon";
            public static final String[] SHIFTS = {MORNING, AFTERNOON};
        }
    }

    public static class Type {
        public static final int CLASSROOM = 0x01;
        public static final int LABORATORY = 0x02;

        public static class Names {
            public static final String CLASSROOM = "Classroom";
            public static final String LABORATORY = "Laboratory";
            public static final String[] TYPES = {CLASSROOM, LABORATORY};
        }
    }

    public static class Careers {
        public static final int IMEC = 0x01;
        public static final int ISIC = 0x02;
        public static final int IIND = 0x03;
        public static final int IELE = 0x04;
        public static final int IELC = 0x05;
        public static final int IBIO = 0x06;
        public static final int IQUI = 0x07;
        public static final int IGEM = 0x08;
        public static final int ILOG = 0x09;

        public static class Names {
            public static final String IMEC = "Ingeniería Mecánica";
            public static final String ISIC = "Ingeniería en Sistemas Computacionales";
            public static final String IIND = "Ingeniería Industrial";
            public static final String IELE = "Ingeniería Electrica";
            public static final String IELC = "Ingeniería Electronica";
            public static final String IBIO = "Ingeniería Bioquimica";
            public static final String IQUI = "Ingeniería Quimica";
            public static final String IGEM = "Ingeniería en Gestion Empresarial";
            public static final String ILOG = "Ingenieria Logistica";
            public static final String[] CAREERS = {ISIC, IMEC, IIND, IELE, IELC, IBIO, IQUI, IGEM, ILOG};
        }
    }

    public static class SelectionMethods {
        public static final int ELITISM = 0x01;
        public static final int TOURNAMENT = 0x02;
        public static final int LINEAR_RANK = 0x03;
        public static final int ROULETTE_WHEEL = 0x04;
        public static final int BINARY_TOURNAMENT = 0x05;
        public static final int STOCHASTIC_UNIVERSAL_SAMPLING = 0x06;

        public static class Names {
            public static final String ELITISM = "Elitism";
            public static final String TOURNAMENT = "Tournament";
            public static final String LINEAR_RANK = "Linear Rank";
            public static final String ROULETTE_WHEEL = "Roulette Wheel";
            public static final String STOCHASTIC_UNIVERSAL_SAMPLING = "Stochastic Universal Sampling";
        }
    }
}
