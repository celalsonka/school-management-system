package com.School_Auto.demo.controller;

import com.School_Auto.demo.entity.Conversation;
import com.School_Auto.demo.entity.Message;
import com.School_Auto.demo.entity.Person;
import com.School_Auto.demo.service.ConversationService;
import com.School_Auto.demo.service.MessageService;
import com.School_Auto.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final MessageService messageService;
    private final ConversationService conversationService;
    private final PersonService personService;
    private final SSEController sseController;

    @Autowired
    public PersonController(PersonService personService, ConversationService conversationService , 
                           MessageService theMessageService,
                           SSEController sseController){
        this.conversationService = conversationService;
        this.personService = personService;
        this.messageService = theMessageService;
        this.sseController = sseController;
    }
    @GetMapping("/HomePage")
    public String showHomePage(Model theModel) {
        Person thePerson = new Person();
        theModel.addAttribute("person",thePerson);
        return "persons/HomePage";  // templates/persons/HomePage.html
    }

    @GetMapping("/list")
    public String listPersons(@RequestParam(value = "id", required = false) Integer excludeId, Model theModel){
      //  kişileri db den al
        List<Person> thePersons;
        Person currentPerson;
        
        if (excludeId != null) {
            // Exclude the person with this ID
            thePersons = personService.findAllBut(excludeId);
            currentPerson = personService.findById(excludeId);
        } else {
            thePersons = personService.findAll();
            currentPerson = new Person();
        }

        // modele veriyi ekle
        theModel.addAttribute("persons" , thePersons);
        theModel.addAttribute("person1", currentPerson);
        return "persons/list-persons";
    }
    @GetMapping("showFormForAdd")
    public String showFormForAdd(Model theModel){
        // create model attribute to bind form data
        Person thePerson = new Person();
        theModel.addAttribute("person",thePerson);
        return "persons/person-form";
    }
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("personId") int theId, Model theModel){
        // get the person from the service
        Person thePerson = personService.findById(theId);
        // set person in the model to prepopulate the form
        theModel.addAttribute("person", thePerson);
        // send over to our form
        return "persons/person-form";
    }


    @PostMapping("/saveMessage")
    public String saveMessage(@ModelAttribute("message") Message theMessage,
                              @RequestParam("personId1") int theId1,
                              @RequestParam("personId2") int theId2,
                              Model theModel) {

        // 1) Conversation'ı bul / yoksa oluştur
        Conversation theConversation = conversationService.findById1AndAd2(theId1, theId2);
        if (theConversation == null) {
            theConversation = new Conversation(theId1, theId2);
            theConversation = conversationService.save(theConversation); // kaydedip id alın
        }

        // 2) Mesaja ilişki, timestamp ve sender name ekle
        theMessage.setConversation(theConversation);
        theMessage.setTimestamp(LocalDateTime.now());
        theMessage.setSenderName("User " + theId1); // Gönderen kişinin ID'si

        // 3) Mesajı kaydet
        messageService.save(theMessage);

        // 4) Güncel mesaj listesini al
        List<Message> messages = messageService.findAllByConversationId(theConversation.getId());

        // 5) SSE ile real-time güncelleme gönder
        sseController.broadcastMessages(theConversation.getId());

        // 6) Model'i doldurup dialog'a döndür
        theModel.addAttribute("person1", personService.findById(theId1));
        theModel.addAttribute("person2", personService.findById(theId2));
        theModel.addAttribute("conversation", theConversation);
        theModel.addAttribute("messages", messages);
        theModel.addAttribute("message", new Message()); // boş form objesi

        return "persons/dialog";
    }
    @GetMapping("/showDialog")
    public String showDialog(@RequestParam("personId1") int theId1,
                             @RequestParam("personId2") int theId2,
                             Model theModel) {

        Person thePerson1 = personService.findById(theId1);
        Person thePerson2 = personService.findById(theId2);

        // Conversation'ı bul (iki yönlü kontrol yapan DAO kullanıyoruz)
        Conversation theConversation = conversationService.findById1AndAd2(theId1, theId2);

        if (theConversation == null) {
            theConversation = new Conversation(theId1, theId2);
            theConversation = conversationService.save(theConversation);
        }

        List<Message> messages = messageService.findAllByConversationId(theConversation.getId());

        theModel.addAttribute("person1", thePerson1);
        theModel.addAttribute("person2", thePerson2);
        theModel.addAttribute("conversation", theConversation);
        theModel.addAttribute("messages", messages);
        theModel.addAttribute("message", new Message()); // form için

        return "persons/dialog";
    }

    @PostMapping("/save")
    public String savePerson(@ModelAttribute("person") Person thePerson, Model theModel){
        Person success = personService.loginPerson(thePerson);
        if(success == null){
            // save the person
            personService.save(thePerson);
            success = personService.loginPerson(thePerson);
            // get all persons from db
            List<Person> thePersons = personService.findAll();

            // add data to model
            theModel.addAttribute("person1", success);
            theModel.addAttribute("persons", thePersons);
            return "persons/HomePage";
        }else{
            theModel.addAttribute("errorMessage", "This user already exists!");
            theModel.addAttribute("person", new Person());
            return "persons/Register";
        }
    }
    @PostMapping("/login")
    public String login(@ModelAttribute("person") Person thePerson, Model theModel) {
        Person success = personService.loginPerson(thePerson);
        if (success != null) {
            theModel.addAttribute("message", "Login successful!");

            List<Person> thePersons = personService.findAllBut(success.getId());

            theModel.addAttribute("person1", success);
            theModel.addAttribute("persons", thePersons);
            return "persons/List-personsForChat";
        } else {
            theModel.addAttribute("error", "Invalid first name or password");
            theModel.addAttribute("person", new Person());
            return "persons/HomePage";
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("personId") int theId){
        // delete the employee
        personService.deleteById(theId);
        // redirect to the / employees/list
        return "redirect:/persons/list";
    }
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("person", new Person());
        return "persons/Register";  // templates/persons/register.html dosyası
    }

}
