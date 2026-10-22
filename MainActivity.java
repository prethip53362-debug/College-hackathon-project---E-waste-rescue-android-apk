package com.example.ewasterescue;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {
    LinearLayout root, content;
    int green = Color.rgb(22, 125, 79), dark = Color.rgb(23, 48, 39), pale = Color.rgb(237, 248, 241);
    int requests = 0, points = 0;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(dark);
        getWindow().setNavigationBarColor(dark);
        showHome();
    }

    GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(radius); return d;
    }
    TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(null, Typeface.BOLD);
        t.setPadding(0, 6, 0, 6); return t;
    }
    void base(String title) {
        root = new LinearLayout(this); root.setOrientation(1); root.setBackgroundColor(Color.rgb(248,251,248));
        TextView top = text("♻  E-WASTE RESCUE", 18, Color.WHITE, true);
        top.setPadding(20, 20, 16, 20); top.setBackgroundColor(dark); root.addView(top);
        ScrollView sc = new ScrollView(this); content = new LinearLayout(this); content.setOrientation(1); content.setPadding(18,16,18,24);
        sc.addView(content); root.addView(sc, new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout nav = new LinearLayout(this); nav.setPadding(8,8,8,8); nav.setBackgroundColor(Color.WHITE);
        String[] labels = {"Home","Recycle","Dashboard"};
        for (String label: labels) { Button b=button(label, green); b.setTextSize(12); nav.addView(b,new LinearLayout.LayoutParams(0,-2,1)); b.setOnClickListener(v->{ if(label.equals("Home"))showHome(); else if(label.equals("Recycle"))showForm(); else showDashboard(); }); }
        root.addView(nav); setContentView(root);
    }
    Button button(String s, int color) {
        Button b = new Button(this); b.setText(s); b.setTextColor(Color.WHITE); b.setAllCaps(false); b.setBackground(bg(color,24)); b.setPadding(12,8,12,8); return b;
    }
    void gap(){ View v=new View(this); content.addView(v,new LinearLayout.LayoutParams(1,12)); }
    void card(String title, String desc) {
        LinearLayout box=new LinearLayout(this); box.setOrientation(1); box.setPadding(16,12,16,12); box.setBackground(bg(Color.WHITE,20));
        box.addView(text(title,17,dark,true)); box.addView(text(desc,14,Color.DKGRAY,false));
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.bottomMargin=12; content.addView(box,p);
    }
    void showHome() {
        base("Home");
        TextView hero=text("Give your old electronics\na new life.",27,Color.WHITE,true); hero.setPadding(18,20,18,8);
        LinearLayout h=new LinearLayout(this); h.setOrientation(1); h.setPadding(8,8,8,18); h.setBackground(bg(green,24)); h.addView(hero);
        TextView sub=text("Responsible recycling starts with one small action.",15,Color.WHITE,false); sub.setPadding(18,0,18,12); h.addView(sub);
        Button start=button("＋  Submit e-waste",dark); start.setOnClickListener(v->showForm()); h.addView(start);
        content.addView(h); gap();
        card("📱  What can you recycle?","Phones • Laptops • Chargers • Batteries • Cables");
        card("🌍  Why it matters","Keep harmful electronic waste out of landfills and help recover reusable materials.");
        card("🏆  Your impact","Points earned: "+points+"   |   Requests submitted: "+requests);
        card("ℹ️  Demo prototype","This hackathon demo stores request counts only while the app is open. It does not contact a real recycler.");
    }
    EditText field(String hint) {
        EditText e=new EditText(this); e.setSingleLine(true); e.setHint(hint); e.setTextSize(15); e.setPadding(14,8,14,8); e.setBackground(bg(Color.WHITE,14));
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.bottomMargin=10; content.addView(e,p); return e;
    }
    void showForm() {
        base("Recycle");
        content.addView(text("Submit e-waste",25,dark,true));
        content.addView(text("Tell us what you want to recycle. This is a demo request form.",14,Color.DKGRAY,false)); gap();
        EditText name=field("Your name"); EditText phone=field("Phone number"); EditText location=field("Pickup area / address");
        Spinner type=new Spinner(this); String[] types={"Mobile phone","Laptop / computer","Charger / cables","Battery","Other electronics"};
        type.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,types));
        content.addView(text("Item type",14,dark,true)); content.addView(type); gap();
        EditText notes=field("Condition / extra details (optional)");
        Button submit=button("Submit demo request",green); content.addView(submit);
        submit.setOnClickListener(v->{
            if(name.getText().toString().trim().isEmpty() || phone.getText().toString().trim().isEmpty() || location.getText().toString().trim().isEmpty()) {
                Toast.makeText(this,"Please enter name, phone and pickup area.",Toast.LENGTH_LONG).show(); return;
            }
            requests++; points+=10;
            new android.app.AlertDialog.Builder(this).setTitle("Request recorded! ♻")
              .setMessage("Thank you, "+name.getText().toString()+"!\nItem: "+types[type.getSelectedItemPosition()]+"\nDemo points earned: +10\n\nPrototype only: no real pickup has been booked.")
              .setPositiveButton("View dashboard",(d,w)->showDashboard()).show();
        });
    }
    void showDashboard() {
        base("Dashboard");
        content.addView(text("Your recycling impact",25,dark,true));
        gap(); card("♻  Requests submitted",String.valueOf(requests));
        card("🏆  Eco points earned",String.valueOf(points));
        card("🌱  Take action","Collect unused electronics safely and hand them to an authorised e-waste recycler.");
        card("📌  Next step for a real product","Connect a backend and verified recycler/pickup service before using real customer data.");
        Button b=button("Submit another item",green); content.addView(b); b.setOnClickListener(v->showForm());
    }
}
