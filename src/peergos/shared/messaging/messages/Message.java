package peergos.shared.messaging.messages;

import peergos.shared.cbor.*;

import java.util.*;

public interface Message extends Cborable {

    Type type();


    static Message fromCbor(Cborable cbor) {
        if (! (cbor instanceof CborObject.CborMap))
            throw new IllegalStateException("Incorrect cbor: " + cbor);
        CborObject.CborMap map = (CborObject.CborMap) cbor;

        Type category = map.get("c", c -> Type.byValue((int) ((CborObject.CborLong) c).value));
        switch (category) {
            case Join: return map.get("p", Join::fromCbor);
            case Invite: return map.get("p", Invite::fromCbor);
            case Application: return map.get("p", ApplicationMessage::fromCbor);
            default: throw new IllegalStateException("Invalid message type!");
        }
    }

    Map<Integer, Type> byValue = new HashMap<>();
    enum Type {
        Join(0),
        Invite(1),
        Application(2);

        public final int value;

        Type(int value) {
            this.value = value;
            byValue.put(value, this);
        }

        public static Type byValue(int val) {
            if (!byValue.containsKey(val))
                throw new IllegalStateException("Unknown message type: " + val);
            return byValue.get(val);
        }
    }
}
