package ru.cbr.ehdapo.customs.publication.longpolling.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cbr.ehdapo.customs.publication.longpolling.dto.Message;
import ru.cbr.ehdapo.util.CheckContext;
import ru.cbr.ehdapo.util.services.UserAccessRightsDistributionService;
import ru.kamatech.commons.objects.UserData;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.sql.CallableStatement;
import java.sql.Types;

import static com.google.common.base.Strings.isNullOrEmpty;

@Repository
@Slf4j
public class UserStatusRepository {

    private EntityManagerFactory entityManagerFactory;

    private UserAccessRightsDistributionService distributionService;


    @Autowired
    public UserStatusRepository(EntityManagerFactory entityManagerFactory, UserAccessRightsDistributionService distributionService) {
        this.entityManagerFactory = entityManagerFactory;
        this.distributionService = distributionService;
    }

    public Message getCurrentStatus(String login) throws Exception {

        if (login.isEmpty()) {
            throw new Exception("login parameter is null");
        }

        distributionService.setUserAccessRights(login);
        Message message = null;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Session session = entityManager.unwrap(Session.class);
        transaction.begin();
        message =
                session.doReturningWork(
                        connection -> {
                            try (CallableStatement function =
                                         connection.prepareCall(
                                                 "{? = call APC$PG_CALC_CONTROL.pGetStopCalcFlag(?,?)} ")) {
                                function.registerOutParameter(1, Types.BIGINT);
                                function.registerOutParameter(3, Types.VARCHAR);
                                function.setString(2, login);
                                function.execute();

                                boolean canActivate = function.getLong(1) == 0;
                                boolean isRunning = isNullOrEmpty(function.getString(3));
                                return new Message(isRunning, function.getString(3), canActivate);
                            }
                        });

        if (transaction.isActive()) {
            transaction.commit();
        }
        entityManager.close();

        return message;
    }


    @CheckContext
    public Message setStopCalcFlgOn(String login) {

        Message message = null;

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Session session = entityManager.unwrap(Session.class);
        transaction.begin();
        message =
                session.doReturningWork(
                        connection -> {
                            try (CallableStatement function =
                                         connection.prepareCall(
                                                 "{? = call APC$PG_CALC_CONTROL.pSetStopCalcFlag_ON(?,?)} ")) {
                                function.registerOutParameter(1, Types.BIGINT);
                                function.registerOutParameter(3, Types.VARCHAR);
                                function.setString(2, login);
                                function.execute();
                                boolean isRunning = function.getLong(1) == 0;
                                return new Message(isRunning, function.getString(3));
                            }
                        });

        if (transaction.isActive()) {
            transaction.commit();
        }

        entityManager.close();
        return message;
    }

    @CheckContext
    public Message setStopCalcFlgOff(String login) {

        Message message = null;

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Session session = entityManager.unwrap(Session.class);
        transaction.begin();
        message =
                session.doReturningWork(
                        connection -> {
                            try (CallableStatement function =
                                         connection.prepareCall(
                                                 "{? = call APC$PG_CALC_CONTROL.pSetStopCalcFlag_OFF(?,?)} ")) { //call APC$PG_CALC_CONTROL.pGetStopCalcFlag(?,?)}
                                function.registerOutParameter(1, Types.BIGINT);
                                function.registerOutParameter(3, Types.VARCHAR);
                                function.setString(2, login);
                                function.execute();
                                boolean isRunning = function.getLong(1) == 0;
                                return new Message(isRunning, function.getString(3));
                            }
                        });

        if (transaction.isActive()) {
            transaction.commit();
        }
        entityManager.close();
        return message;
    }
}
