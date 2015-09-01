package charls.dontcallme;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import charls.dontcallme.Activities.ContactosActivity;
import charls.dontcallme.Activities.ListaNegraActivity;
import charls.dontcallme.Application.DontCalleMeApplication;
import charls.dontcallme.Database.Database;
import charls.dontcallme.ItemManagers.ItemManager;
import charls.dontcallme.ListAdapters.TelefonoAdapter;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<DontCalleMeApplication> {

    public ApplicationTest() {
        super(DontCalleMeApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
}