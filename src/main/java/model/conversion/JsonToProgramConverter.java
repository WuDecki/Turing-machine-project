package model.conversion;

import model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonToProgramConverter {

    private JSONObject jsonObject;
    private TuringMachineDefinition definition;

    public JsonToProgramConverter(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public TuringMachineProgram convert() throws JSONException, InvalidConversionException {
        definition = new TuringMachineDefinition();
        convertSymbols();
        convertStates();
        convertFirstState();
        convertMovementSymbol();
        convertPommelStartPosition();
        return prepareTuringMachineProgram();
    }

    private void convertSymbols() throws JSONException {
        List<String> rawSymbols = extractList(jsonObject.getJSONArray("symbols"));

        List<Character> symbols = new ArrayList<>();
        for(String rawSymbol: rawSymbols) {
            symbols.add(rawSymbol.toCharArray()[0]);
        }

        definition.setSymbols(symbols);
    }

    private void convertStates() throws JSONException, InvalidConversionException {
        List<String> statesIdn = extractList(jsonObject.getJSONArray("states"));
        JSONObject program = jsonObject.getJSONObject("program");
        definition.setStates(createStates(statesIdn, program));
        for(String stateIdn: statesIdn) {
            Object rawState = program.get(stateIdn);
            State state = definition.getStateByIdn(stateIdn);
            prepareOperations(state, rawState);
        }
    }

    private List<State> createStates(List<String> statesIdn, JSONObject program) throws JSONException {
        List<State> states = new ArrayList<>();

        for(String stateIdn: statesIdn) {
            Object rawState = program.get(stateIdn);
            StateType stateType = chooseStateType(rawState);
            states.add(new State(stateIdn, stateType));
        }

        return states;
    }

    private void prepareOperations(State state, Object rawState) throws JSONException, InvalidConversionException {
        if (!state.getType().equals(StateType.NORMAL))
            return;

        for (Character symbol: definition.getSymbols()) {
            JSONArray array = ((JSONObject)rawState).getJSONArray(symbol.toString());
            Character newCharacter = array.getString(0).charAt(0);
            PommelMovement movement = getPommelMovement(array.getString(1));
            String nextStateIdn = array.getString(2);
            State nextState = definition.getStateByIdn(nextStateIdn);
            Operation operation = new Operation(newCharacter, movement, nextState);
            state.addOperation(symbol, operation);
        }
    }

    private PommelMovement getPommelMovement(String movementSymbol) throws InvalidConversionException {
        switch (movementSymbol) {
            case "P": {
                return PommelMovement.RIGHT;
            }
            case "L": {
                return PommelMovement.LEFT;
            }
            case "-": {
                return PommelMovement.NONE;
            }
            default: throw new InvalidConversionException("Invalid pommel movement symbol");
        }
    }

    private StateType chooseStateType(Object rawState) {
        if (rawState instanceof String) {
            String stateType = (String)rawState;
            if (stateType.equals("SA")) {
                return StateType.ACCEPTABLE;
            } else {
                return StateType.REJECTABLE;
            }
        } else{
            return StateType.NORMAL;
        }
    }

    private static List<String> extractList(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }

        return list;
    }

    private void convertFirstState() throws JSONException {
        String firstStateIdn = jsonObject.getString("firstState");
        State firstState = definition.getStateByIdn(firstStateIdn);
        definition.setFirstState(firstState);
    }

    private void convertMovementSymbol() throws JSONException {
        String movementSymbol = jsonObject.getString("movementSymbol");
        definition.setMovementCharacter(movementSymbol.charAt(0));
    }

    private void convertPommelStartPosition() throws JSONException, InvalidConversionException {
        String pommelStartPosition = jsonObject.getString("pommelStartPosition");
        definition.setPommelStartPosition(choosePommelStartPosition(pommelStartPosition));
    }

    private PommelStartPosition choosePommelStartPosition(String pommelStartPosition) throws InvalidConversionException {
        switch (pommelStartPosition) {
            case "P": return PommelStartPosition.BEGINNING;
            case "K": return PommelStartPosition.END;
            default: throw new InvalidConversionException("Invalid pommel start position symbol");
        }
    }

    private TuringMachineProgram prepareTuringMachineProgram() {
        return new TuringMachineProgram(definition.getSymbols(), definition.getMovementCharacter(),
                definition.getStates(), definition.getFirstState(), definition.getPommelStartPosition());
    }
}
