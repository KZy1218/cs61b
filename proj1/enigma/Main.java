package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Yi Zhang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        _newMachine = readConfig();
        String setupInfo = _input.nextLine();
        readAndSet(setupInfo);

        StringBuilder encrypted = new StringBuilder();
        while (_input.hasNextLine()) {
            String nextLine = _input.nextLine();
            if (nextLine.trim().isEmpty()) {
                encrypted.append("\n");
                continue;
            }
            if (nextLine.startsWith("*")) {
                readAndSet(nextLine);
            } else {
                nextLine = nextLine.replaceAll("\\s+", "");
                encrypted.append(insertSpace(_newMachine.convert(nextLine)));
                encrypted.append("\n");
            }
        }
        printMessageLine(encrypted.toString());
    }

    /** Read the setting through SETUPCONFIG and set up the Machine. */
    private void readAndSet(String setupConfig) {
        Scanner setupScan = new Scanner(setupConfig);
        if (!setupScan.next().equals("*")) {
            throw new EnigmaException("Incorrect input file. "
                    + "File should start with *");
        }
        selectRotor(setupScan);

        String setting = setupScan.next();
        if (isValidName(setting)) {
            throw new EnigmaException("Extra rotor added");
        }

        if (setting.length() > _total - 1) {
            throw new EnigmaException("Incorrect setting format");
        }
        setUp(_newMachine, setting);

        if (setupScan.hasNext()) {
            String ringOrCycle = setupScan.next();
            if (ringOrCycle.charAt(0) != '(') {
                setUpRing(_newMachine, ringOrCycle);
            } else {
                _first = ringOrCycle;
            }
        }

        readCycle(setupScan);
    }

    /** Use scanner S to read which rotors we want to use. */
    private void selectRotor(Scanner s) {
        String[] chosen = new String[_total];
        int count = 0;
        while (count < _total) {
            String potentialName = s.next();
            if (isValidName(potentialName)) {
                chosen[count] = potentialName;
            } else {
                throw new EnigmaException("Incorrect rotor name");
            }
            count += 1;
        }

        if (CheckDuplicate.checkRotor(chosen)) {
            _newMachine.insertRotors(chosen);
        } else {
            throw new EnigmaException("Rotors can only be used once");
        }
    }

    /** Use Scanner S to Read the cycle if any. */
    private void readCycle(Scanner s) {
        StringBuilder pair = new StringBuilder();
        if (s.hasNext()) {
            if (!_first.equals("")) {
                pair.append(_first);
            }
            String info = s.next();
            while (checkValid(info)) {
                pair.append(info);
                if (s.hasNext()) {
                    info = s.next();
                } else {
                    break;
                }
            }
            if (pair.toString().equals("")) {
                throw new EnigmaException("Incorrect cycle format");
            }
            try {
                String cycle = pair.toString();
                Permutation plugboard = new Permutation(cycle, _alphabet);
                _newMachine.setPlugboard(plugboard);
            } catch (EnigmaException e) {
                throw error("Incorrect cycle format");
            }
        } else {
            Permutation plugboard = new Permutation(_first, _alphabet);
            _newMachine.setPlugboard(plugboard);
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.nextLine());

            int numRotor = _total = _config.nextInt();
            int pawls = _pawl = _config.nextInt();
            assert (numRotor > pawls);

            while (_config.hasNext()) {
                _allRotors.add(readRotor());
            }
            _config.close();
            return new Machine(_alphabet, numRotor, pawls, _allRotors);

        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String thisName;
            if (_nextName.equals("")) {
                thisName = _config.next();
            } else {
                thisName = _nextName;
            }
            String notch = _config.next();
            char type = notch.charAt(0);

            StringBuilder cyclesBuilder = new StringBuilder();
            String info = _config.next();
            while (checkValid(info)) {
                cyclesBuilder.append(info);
                if (_config.hasNext()) {
                    info = _config.next();
                } else {
                    break;
                }
            }
            _nextName = info;

            String cycle = cyclesBuilder.toString();
            Permutation p = new Permutation(cycle, _alphabet);

            if (type == 'M') {
                return new MovingRotor(thisName, p, notch);
            } else if (type == 'N') {
                return new FixedRotor(thisName, p);
            } else {
                return new Reflector(thisName, p);
            }

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Check whether a String S is in cycle format, return true if
     *  S is of correct format. */
    public static boolean checkValid(String s) {
        Pattern pa = Pattern.compile("^([(][^()*,]+[)])+$");
        Matcher ma = pa.matcher(s);
        return ma.matches();
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Set M's rings according to the specification given on RINGS,
     *  which must have the format specified in the assignment. */
    private void setUpRing(Machine M, String rings) {
        M.setRings(rings);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        _output.print(msg);
    }

    /** Insert a space after every 5 characters for string S and return
     *  the modified string. */
    private String insertSpace(String s) {
        int len;
        if (s.length() % 5 == 0) {
            len = s.length() / 5;
        } else {
            len = (s.length() / 5) + 1;
        }
        String[] pieces = new String[len];
        if (s.length() <= 5) {
            return s;
        } else {
            for (int i = 1; i <= (len); i++) {
                if (5 * i > s.length()) {
                    pieces[i - 1] = s.substring(5 * (i - 1));
                } else {
                    pieces[i - 1] = s.substring(5 * (i - 1), 5 * i);
                }
            }
        }
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < pieces.length; i++) {
            if (i == pieces.length - 1) {
                res.append(pieces[i]);
            } else {
                res.append(pieces[i]).append(" ");
            }
        }
        return res.toString();
    }

    /** Verify whether the given string S is a valid rotor name.
     *  If so return true. */
    private boolean isValidName(String s) {
        for (Rotor rotor : _allRotors) {
            if (rotor.name().equals(s)) {
                return true;
            }
        }
        return false;
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** The collection of all available rotors. */
    private ArrayList<Rotor> _allRotors = new ArrayList<>();

    /** Next rotor's name. */
    private String _nextName = "";

    /** Total number of rotors. */
    private int _total;

    /** Total number of moving rotors. */
    private int _pawl;

    /** The machine that is currently being used. */
    private Machine _newMachine;

    /** The first cycle of the plugboard. */
    private String _first = "";

}
