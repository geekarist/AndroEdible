package com.example.androedible.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import static java.lang.String.format;

public class MainActivity extends ActionBarActivity {

    private class Intake {
        private Date date;
        private int total;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "Intake{" +
                    "date=" + date +
                    ", total=" + total +
                    '}';
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View mainView = findViewById(R.layout.activity_main);
        TextView child = (TextView) findViewById(R.id.hello);

        DB db = null;
        try {
            Properties properties = new Properties();
            properties.load(getClassLoader().getResourceAsStream("env.properties"));
            String uri = properties.getProperty("db.uri");
            db = new MongoClient(new MongoClientURI(uri)).getDB("getstronger");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Jongo jongo = new Jongo(db);
        MongoCollection calories = jongo.getCollection("calories");

        String startOfWeek = "2014-04-28T00:00:00.000Z";
        String endOfWeek = "2014-04-04T23:59:59.999Z";
        Iterable<Intake> all = calories.find(format("{date: {$gt: '%s', $lt: '%s'}", startOfWeek, endOfWeek)).as(Intake.class);
        StringBuilder intakes = new StringBuilder();
        for (Intake intake : all) {
            intakes.append(intake.toString() + '\n');
        }
        child.setText(intakes);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
