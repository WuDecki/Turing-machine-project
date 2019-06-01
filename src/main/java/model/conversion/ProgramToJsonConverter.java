package model.conversion;

import model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProgramToJsonConverter {

    private TuringMachineProgram program;

    public ProgramToJsonConverter(TuringMachineProgram program) {
        this.program = program;
    }

    public JSONObject convert() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("symbols", convertSymbols());
        jsonObject.put("movementSymbol", program.getMovementCharacter());
        jsonObject.put("states", convertStates());
        jsonObject.put("firstState", program.getFirstState().getIdn());
        jsonObject.put("pommelStartPosition", convertPommelStartPosition());
        jsonObject.put("program", convertProgram());

        return jsonObject;
    }

    private JSONArray convertSymbols() {
        JSONArray jsonSymbols = new JSONArray();
        for (Character symbol: program.getSymbols()) {
            jsonSymbols.put(symbol);
        }

        return jsonSymbols;
    }

    private JSONArray convertStates() {
        JSONArray jsonStates = new JSONArray();
        for (State state: program.getStates()) {
            jsonStates.put(state.getIdn());
        }

        return jsonStates;
    }

    private String convertPommelStartPosition() {
        switch (program.getStartPosition()) {
            case BEGINNING: return "P";
            case END: return "K";
            default: {
                //TODO Wyrzucic wyjatek
                return null;
            }
        }
    }

    private JSONObject convertProgram() throws JSONException {
        JSONObject jsonProgram = new JSONObject();
        for (State state: program.getStates()) {
            jsonProgram.put(state.getIdn(), convertState(state));
        }

        return jsonProgram;
    }

    private Object convertState(State state) throws JSONException {
        JSONObject jsonState = new JSONObject();

        if (state.getType().equals(StateType.ACCEPTABLE))
            return "SA";
        if (state.getType().equals(StateType.REJECTABLE))
            return "SN";

        for (Character symbol: program.getSymbols()) {
            jsonState.put(symbol.toString(), convertOperation(state.getOperation(symbol)));
        }

        return jsonState;
    }

    private JSONArray convertOperation(Operation operation) {
        JSONArray jsonOperation = new JSONArray();
        jsonOperation.put(operation.getNewChar());
        jsonOperation.put(convertPommelMovement(operation.getMovement()));
        jsonOperation.put(operation.getNextState().getIdn());

        return jsonOperation;
    }

    private String convertPommelMovement(PommelMovement pommelMovement) {
        switch (pommelMovement) {
            case LEFT: return "L";
            case RIGHT: return "P";
            case NONE: return "-";
            default: {
                //TODO Wyrzucic wyjatek
                return null;
            }
        }
    }
}
