package net.rocketeer.chirp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab2 extends Fragment {

    public tab2() {
        // Required empty public constructor
    }

    private ArrayList<String> myList;
    private File directory;
    private ListView listView;
    private ArrayAdapter adapter;
    private File[] list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        listView = (ListView) view.findViewById(R.id.audioListView);
        directory = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "chirp");

        myList = new ArrayList<String>();
        listFiles();
//        list = directory.listFiles();
//
//        for (int i = 0; i < list.length; i++) {
//            myList.add(list[i].getName());
//        }

        registerForContextMenu(listView);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, myList);
        //Set all the file in the list.
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setCacheColorHint(Color.TRANSPARENT);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    File audioFiles = new File(directory + File.separator + list[position].getName());
                    Uri audioUri = FileProvider.getUriForFile(getContext(), view.getContext().getPackageName() + ".provider", audioFiles);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(audioUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        return view;
    }

    public void listFiles(){
        list = directory.listFiles();
        for (int i = 0; i < list.length; i++) {
            myList.add(list[i].getName());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId() == R.id.audioListView) {
                String[] actions = getResources().getStringArray(R.array.context_menu);
                for (int i = 0; i < actions.length; i++) {
                    menu.add(Menu.NONE, i, i, actions[i]);
                }
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;
        String fileName = myList.get(itemPosition);

        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.context_menu);
        String menuItemName = menuItems[menuItemIndex];

        File currentName = new File(directory + File.separator + fileName);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        switch (menuItemName) {

            case "Edit":

                alert.setTitle("Rename");
                alert.setMessage("Current filename: " + fileName);
                EditText input = new EditText(getActivity());
                alert.setView(input);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String stringInput = input.getEditableText().toString().trim();
                        String ext = ".3gp";

                            if(stringInput.equals("")){
                                Toast.makeText(getActivity(), "Please enter a name for the file", Toast.LENGTH_LONG).show();
                            }
                            else{
                                File newName = new File(directory + File.separator + stringInput + ext);
                                currentName.renameTo(newName);

                                // update data in adapter
                                adapter.clear();
                                listFiles();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "File Renamed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                alert.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                alert.create();
                alert.show();

                break;

            case "Delete":

                alert.setTitle("Delete " + fileName + " ?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                currentName.delete();
                              //  adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, myList);
                                adapter.remove(fileName);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "File Deleted", Toast.LENGTH_SHORT).show();
                            }

                        });
                alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                alert.create();
                alert.show();

                break;

        }
        return true;

    }


}

