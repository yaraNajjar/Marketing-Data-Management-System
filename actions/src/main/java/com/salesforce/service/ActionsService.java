package com.salesforce.service;

import com.salesforce.Helper.Helper;
import com.salesforce.bean.Action;
import com.salesforce.bean.ActionType;
import com.salesforce.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class ActionsService {
    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private Helper helper;

    public List<Action> getAllActionsActive() {
        return actionRepository.findAllByDeletedFalse();
    }


    public boolean isUsing(long metricId) {
        List<Action> allActiveActions = actionRepository.findAll().stream()
                .filter(action -> !action.isDeleted() && action.isEnabled())
                .toList();
        for (Action action : allActiveActions) {
            if (action.getConditions() != null && !action.getConditions().isEmpty()) {
                for (List<Integer> condition : action.getConditions()) {
                    if (condition.contains((int) metricId)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean deleteActionsByAccountId(long accountId)  {
        try{
            List<Action> actions = actionRepository.findActionsByAccountId(accountId);
            for(Action a :actions){
                actionRepository.delete(a);
            }
            return true;
        }catch (Exception e){
            return false;
        }

    }


    public Action getActionById(long id) {
        return actionRepository.findActionById(id);
    }

    public List<Action> getAllActionsHistory() {
        return actionRepository.findByDeletedTrue();
    }

    public List<Action> getAllActionsByAccountIdActive(long accountId) {
        return actionRepository.findByAccountIdAndDeletedFalse(accountId);
    }

    public List<Action> getAllActionsByAccountIdHistory(long accountId) {
        return actionRepository.findByAccountIdAndDeletedTrue(accountId);
    }

    public String createAction(Action action) throws Exception {
        validationAction(action);
        LocalDate currentDate = LocalDate.now();
        String today = currentDate.format(DateTimeFormatter.ISO_DATE);
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        action.setCreateDate(today);
        action.setLastUpdate(String.valueOf(currentTime));
        actionRepository.save(action);
        return action.getName();
    }

    public String updateAction(long id, Action action) throws Exception {
        Action existingAction = actionRepository.findById(id)
                .orElseThrow(() -> new Exception("Action with ID " + id + " not found"));
        validationAction(action);
        existingAction.setName(action.getName());
        existingAction.setConditions(action.getConditions());
        existingAction.setActionType(action.getActionType());
        existingAction.setRunOnTime(action.getRunOnTime());
        existingAction.setRunOnDay(action.getRunOnDay());
        existingAction.setNotificationText(action.getNotificationText());
        existingAction.setSendTo(action.getSendTo());
        existingAction.setDeleted(action.isDeleted());
        existingAction.setEnabled(action.isEnabled());
        existingAction.setLastUpdate(String.valueOf(Timestamp.valueOf(LocalDateTime.now())));
        actionRepository.save(existingAction);
        return existingAction.getName();
    }

    public String deleteAction(Long id) throws Exception {
        Assert.notNull(id, "ID must not be null");
        Optional<Action> optionalAction = actionRepository.findById(id);
        if (optionalAction.isPresent()) {
            Action action = optionalAction.get();
            action.setDeleted(true);
            actionRepository.save(action);
            return action.getName();
        } else {
            throw new Exception("Action with ID " + id + " does not exist");
        }
    }

    public String disableAction(Long id) throws Exception {
        Assert.notNull(id, "ID must not be null");
        Optional<Action> optionalAction = actionRepository.findById(id);
        if (optionalAction.isPresent()) {
            Action action = optionalAction.get();
            action.setEnabled(false);
            actionRepository.save(action);
            return action.getName();
        } else {
            throw new Exception("Action with ID " + id + " does not exist");
        }
    }

    public String enableAction(Long id) throws Exception {
        Assert.notNull(id, "ID must not be null");
        Optional<Action> optionalAction = actionRepository.findById(id);
        if (optionalAction.isPresent()) {
            Action action = optionalAction.get();
            action.setEnabled(true);
            actionRepository.save(action);
            return action.getName();
        } else {
            throw new Exception("Action with ID " + id + " does not exist");
        }
    }


    public String undeleteAction(Long id) throws Exception {
        if (actionRepository.existsById(id)) {
            Assert.notNull(id, "ID must not be null");
            Optional<Action> optionalAction = actionRepository.findById(id);
            if (optionalAction.isPresent()) {
                Action action = optionalAction.get();
                action.setDeleted(false);
                actionRepository.save(action);
                return action.getName();
            } else {
                throw new Exception("Something wrong try again");
            }
        } else {

            throw new Exception("Action does not exist");
        }
    }

    private boolean existingAction(Action action) {
        Optional<Action> existingAction = actionRepository.findByAccountIdAndActionTypeAndRunOnTimeAndRunOnDayAndNotificationTextAndSendToAndDeletedAndEnabled(
                action.getAccountId(),
                action.getActionType(),
                action.getRunOnTime(),
                action.getRunOnDay(),
                action.getNotificationText(),
                action.getSendTo(),
                action.isDeleted(),
                action.isEnabled()
        );

        return existingAction.isPresent();
    }
//    private boolean conditionExisting(List<List<Integer>> newCondition) {
//        return list1 != null && list2 != null && list1.size() == list2.size() &&
//                list1.stream().allMatch(innerList1 -> list2.stream().anyMatch(innerList1::equals));
//        return false;
//    }


    private void validationAction(Action action) throws Exception {
        if (existingAction(action)) {
            throw new Exception("Failed : Action with the same attributes already exists");
        }
        if (action.getName() == null || action.getName().isEmpty()) {
            throw new Exception("Failed : Input Name empty or not valid.");
        }
        if (action.getConditions() == null|| action.getConditions().isEmpty()) {
            throw new Exception("Failed : Input Conditions empty or not valid.");
        }
        if (!action.getActionType().equals(ActionType.SMS) &&
                !action.getActionType().equals(ActionType.WHATSAPP) &&
                !action.getActionType().equals(ActionType.EMAIL)) {
            throw new Exception("Failed : Input Action type empty or not valid.");
        }
        if (action.getRunOnTime() == null ||
                action.getRunOnTime().isEmpty() ||
                (!action.getRunOnTime().endsWith("am") &&
                        !action.getRunOnTime().endsWith("pm"))) {
            throw new Exception("Failed:  Input Run on time empty or not valid.");
        }

        if (!helper.getDays().contains(action.getRunOnDay().toString())) {
            System.out.println(action.getRunOnDay().toString());
            throw new Exception("Failed : Input Metric Action type empty or not valid. " +
                    "Please choose one from : " +
                    "SUNDAY/MONDAY/TUESDAY/WEDNESDAY/THURSDAY/FRIDAY/SATURDAY/DAILY" );
        }
        if (action.getNotificationText() == null || action.getNotificationText().isEmpty()) {
            throw new Exception("Failed : Input Notification Text empty or not valid.");
        }
        if (action.getSendTo() == null
                || action.getSendTo().isEmpty()
                || !helper.isValidSendTo(action.getSendTo(),action.getActionType().toString())) {
            throw new Exception("Failed : Input Send To empty or not valid.");
        }
    }
}

