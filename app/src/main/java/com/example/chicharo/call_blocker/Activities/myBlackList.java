package com.example.chicharo.call_blocker.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chicharo.call_blocker.Adapters.BlockedContactsAdapter;
import com.example.chicharo.call_blocker.DataBases.PhonesDataSource;
import com.example.chicharo.call_blocker.Models.contactModel;
import com.example.chicharo.call_blocker.R;
import java.util.List;

public class myBlackList extends ActionBarActivity implements View.OnClickListener{
    PhonesDataSource phonesDataSource;
    private static final String regexIsAValidPhoneNumber = "^[0-9]{8,12}$";
    private EditText editTextAddNewPhone;
    private BlockedContactsAdapter blockedContactsAdapter;
    List<contactModel> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myblacklist_layout);
        editTextAddNewPhone = (EditText)findViewById(R.id.edit_newPhone);
        Button btnAddNewPhone = (Button)findViewById(R.id.btn_newPhone);
        btnAddNewPhone.setOnClickListener(this);
        Button btnDeleletePhone = (Button)findViewById(R.id.btn_DeleteAll);
        btnDeleletePhone.setOnClickListener(this);
        phonesDataSource = new PhonesDataSource(this);
        phonesDataSource.open();
        prepareRecyclerView();
    }

    private void prepareRecyclerView(){
        RecyclerView recyclerViewBlockedContacts = (RecyclerView)findViewById(R.id.recycler_blocked_contacts);
        recyclerViewBlockedContacts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBlockedContacts.setItemAnimator(new DefaultItemAnimator());
        values = phonesDataSource.getAllContacts();
        blockedContactsAdapter = new BlockedContactsAdapter(values);
        recyclerViewBlockedContacts.setAdapter(blockedContactsAdapter);
        ItemTouchHelper swipeToDismiss = buildSwipeToDismiss();
        swipeToDismiss.attachToRecyclerView(recyclerViewBlockedContacts);
    }

    private ItemTouchHelper buildSwipeToDismiss(){
        ItemTouchHelper swipeToDismiss = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                phonesDataSource.deleteBlockedContact(values.get(viewHolder.getAdapterPosition()));
                values.remove(viewHolder.getAdapterPosition());
                blockedContactsAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        return swipeToDismiss;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_newPhone) {
            if(editTextAddNewPhone.getText().toString().matches(regexIsAValidPhoneNumber)) {
                contactModel contactModel = phonesDataSource.createBlockedContact("Pedro", editTextAddNewPhone.getText().toString());
                values.add(contactModel);
                blockedContactsAdapter.notifyItemInserted(values.indexOf(contactModel));
            } else {
                Toast.makeText(getApplicationContext(), "This is not a valid phone number",
                        Toast.LENGTH_SHORT).show();
            }
        } else if(v.getId() == R.id.btn_DeleteAll){
            phonesDataSource.deleteAll();
            blockedContactsAdapter.notifyItemRangeRemoved(0, values.size());
            values.clear();
        }
    }

    @Override
    protected void onResume() {
        phonesDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        phonesDataSource.close();
        super.onPause();
    }
}