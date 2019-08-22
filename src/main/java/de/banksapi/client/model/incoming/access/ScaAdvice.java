package de.banksapi.client.model.incoming.access;

import de.banksapi.client.model.incoming.Messages;
import de.banksapi.client.model.incoming.Relations;

import java.util.Collection;

public interface ScaAdvice extends Messages, Relations {

    Collection<TanMedium> getTanMedien();

    Collection<Sicherheitsverfahren> getSicherheitsverfahren();

    Sicherheitsverfahren getAktivesSicherheitsverfahren();

    Challenge getChallenge();

}
