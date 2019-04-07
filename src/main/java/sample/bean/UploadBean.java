package sample.bean;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;



/**
 */
@Model
@Slf4j
public class UploadBean {
    private Part file;


    @Inject
    private Event<Part> event;

    public void submit() {
        log.info( "start uploading file");
        event.fire(this.file);
        // give message file already uploaded
        FacesContext faces = FacesContext.getCurrentInstance();
        faces.addMessage(null, new FacesMessage("Success Upload File"));
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
}