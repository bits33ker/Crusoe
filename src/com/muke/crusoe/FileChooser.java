package com.muke.crusoe;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.DateFormat;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FileChooser extends ListActivity {

    private File currentDir;
    private FileArrayAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File("/sdcard/");
        fill(currentDir);
    }
    class Item implements Comparable<Item>{
        private String name;
        private String data;
        private String date;
        private String path;
        private String image;
       
        public Item(String n,String d, String dt, String p, String img)
        {
                name = n;
                data = d;
                date = dt;
                path = p;
                image = img;           
        }
        public String getName()
        {
                return name;
        }
        public String getData()
        {
                return data;
        }
        public String getDate()
        {
                return date;
        }
        public String getPath()
        {
                return path;
        }
        public String getImage() {
                return image;
        }
        public int compareTo(Item o) {
                if(this.name != null)
                        return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
                else
                        throw new IllegalArgumentException();
        }
    }
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
                 this.setTitle("Current Dir: "+f.getName());
                 List<Item>dir = new ArrayList<Item>();
                 List<Item>fls = new ArrayList<Item>();
                 try{
                         for(File ff: dirs)
                         {
                                Date lastModDate = new Date(ff.lastModified());
                                DateFormat formater = DateFormat.getDateTimeInstance();
                                String date_modify = formater.format(lastModDate);
                                if(ff.isDirectory()){
                                       
                                       
                                        File[] fbuf = ff.listFiles();
                                        int buf = 0;
                                        if(fbuf != null){
                                                buf = fbuf.length;
                                        }
                                        else buf = 0;
                                        String num_item = String.valueOf(buf);
                                        if(buf == 0) num_item = num_item + " item";
                                        else num_item = num_item + " items";
                                       
                                        //String formated = lastModDate.toString();
                                        dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"dir_icon128"));
                                }
                                else
                                {
                                	if(ff.getName().contains(".gpx"))
                                        fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"xml_icon128"));
                                }
                         }
                 }catch(Exception e)
                 {   
                         
                 }
                 Collections.sort(dir);
                 Collections.sort(fls);
                 dir.addAll(fls);
                 if(!f.getName().equalsIgnoreCase("sdcard"))
                         dir.add(0,new Item("..","Parent Directory","",f.getParent(),"parent_directory128"));
                 adapter = new FileArrayAdapter(FileChooser.this,R.layout.filechooser,dir);
                 this.setListAdapter((ListAdapter) adapter);
    }
    @Override
        protected void onListItemClick(ListView l, View v, int position, long id) {
                // TODO Auto-generated method stub
                super.onListItemClick(l, v, position, id);
                Item o = adapter.getItem(position);
                if(o.getImage().equalsIgnoreCase("dir_icon128")||o.getImage().equalsIgnoreCase("parent_directory128")){
                                currentDir = new File(o.getPath());
                                fill(currentDir);
                }
                else
                {
                        onFileClick(o);
                }
        }
    private void onFileClick(Item o)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("RESULT",currentDir.toString() +"/"+ o.getName());
        //intent.putExtra("GetFileName",o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
    public class FileArrayAdapter extends ArrayAdapter<Item>{

        private Context c;
        private int id;
        private List<Item>items;
       
        public FileArrayAdapter(Context context, int textViewResourceId,
                        List<Item> objects) {
                super(context, textViewResourceId, objects);
                c = context;
                id = textViewResourceId;
                items = objects;
        }
        public Item getItem(int i)
         {
                 return items.get(i);
         }
         @Override
       public View getView(int position, View convertView, ViewGroup parent) {
             View v = convertView;
        	 try{
               if (v == null) {
                   LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                   v = vi.inflate(R.layout.file_item, null);
               }
              
               /* create a new view of my layout and inflate it in the row */
                //convertView = ( RelativeLayout ) inflater.inflate( resource, null );
               
               final Item o = items.get(position);
               if (o != null) {
                       TextView t1 = (TextView) v.findViewById(R.id.TextView01);
                       TextView t2 = (TextView) v.findViewById(R.id.TextView02);
                       TextView t3 = (TextView) v.findViewById(R.id.TextViewDate);
                       /* Take the ImageView from layout and set the city's image */
                                ImageView imageCity = (ImageView) v.findViewById(R.id.fd_Icon1);
                                String uri = "drawable/" + o.getImage();
                           int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
                           Drawable image = c.getResources().getDrawable(imageResource);
                           imageCity.setImageDrawable(image);
                      
                       if(t1!=null)
                       t1.setText(o.getName());
                       if(t2!=null)
                                t2.setText(o.getData());
                       if(t3!=null)
                                t3.setText(o.getDate());
               }
        	 }
             catch(Exception e)
             {
          	   Log.i("ERROR", "getView: " + e.getMessage());
             }
             return v;
         }
    }
}