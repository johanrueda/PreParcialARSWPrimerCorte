package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.MalformedParametersException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/fraud-bank-accounts")
public class MoneyLaunderingController {

    @Autowired
    @Qualifier("MoneyLaunderingServiceStub")
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping( method = RequestMethod.GET)
    public ResponseEntity<?> getSuspectAccounts() {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getSuspectAccounts(), HttpStatus.OK);
        } catch (MalformedParametersException e) {
            Logger.getLogger(MoneyLaunderingController.class.getName()).log(Level.SEVERE,e.getMessage(),e);
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addSuspectAccounts(@RequestBody SuspectAccount account) throws Exception {
        moneyLaunderingService.addSuspect(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="/{accountId}",method = RequestMethod.GET)
    public ResponseEntity<?> getAccountByID(@PathVariable String accountId)
    {
        return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(accountId), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value="/{accountId}", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizarCuenta(@RequestBody SuspectAccount account) {
        moneyLaunderingService.updateAccountStatus(account);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
