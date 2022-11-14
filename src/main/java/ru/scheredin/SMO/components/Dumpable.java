package ru.scheredin.SMO.components;

import ru.scheredin.SMO.dto.Request;

import java.util.ArrayList;

public interface Dumpable {
    ArrayList<Request> getDump();
}
