package com.example.outofenergy;

public class task_Add_Class {
  public String  id, nazvanie,category_of_task ,instruction, time,  reward,  uploadUri;


    public task_Add_Class() {
                                //schuka vazhnaya chstb coda bez nee zaprosy nixuya ne rabotayut
    }
    public task_Add_Class(String id, String nazvanie,String category_of_task, String instruction, String time, String reward,String uploadUri) {
     this.id = id;
     this.nazvanie = nazvanie;
     this.category_of_task = category_of_task;
     this.instruction = instruction;
     this.time = time;
     this.reward = reward;
     this.uploadUri=uploadUri;
    }

}
