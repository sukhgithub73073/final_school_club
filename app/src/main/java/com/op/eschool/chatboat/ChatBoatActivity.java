package com.op.eschool.chatboat;

import static com.op.eschool.util.Constants.ANIMATED_DAILOG_TYPE_FAILED;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.op.eschool.R;
import com.op.eschool.base.BaseActivity;
import com.op.eschool.databinding.ActivityChatBoatBinding;
import com.op.eschool.models.LocationDetailModel;
import com.op.eschool.models.chatboat_model.ChatboatModel;
import com.op.eschool.util.FToast;
import com.op.eschool.util.Utility;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatBoatActivity extends BaseActivity {
    ActivityChatBoatBinding binding;
    List<ChatboatModel> list = new ArrayList<>() ;
    public static final int REQ_CODE_SPEECH_INPUT = 1;
    public Intent recognizer_intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    public ArrayList<String> text = new ArrayList<>();
    String RESPONSE ="";

    ArrayList<String> badWords = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this , R.layout.activity_chat_boat);

        badWords.add("sex" ) ;
        badWords.add("sax" ) ;
        badWords.add("fuck" ) ;
        badWords.add("f***" ) ;


        binding.back.setOnClickListener(v->{onBackPressed();}) ;
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        binding.mic.setOnClickListener(v->{
            try {
                micIntent.launch(recognizer_intent);
            }
            catch (Exception a) {
                FToast.makeText(getApplicationContext(), "Opps! Your device doesn’t support Speech to Text", FToast.LENGTH_SHORT).show();
            }
        });

        sendMessageSocket("hello") ;
        binding.setMessagesAdapter(new MessagesAdapter(list , model->{
            list.get(list.size()-1).setSuggests(new ArrayList<>()) ;
            binding.getMessagesAdapter().notifyDataSetChanged() ;
            list.add(new ChatboatModel("1",""+model.getMessage(),"SENT",new ArrayList<>())) ;
            int newPosition = list.size() - 1;
            binding.getMessagesAdapter().notifyItemInserted(newPosition);
            binding.recyclerView.scrollToPosition(newPosition);
            sendMessageSocket(model.getMessage()) ;
        }));

        binding.msgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.msgInput.getText().toString();
                if(message.length() != 0){
                    if (!detactBadWord(message)){
                    list.add(new ChatboatModel("1",""+message,"SENT",new ArrayList<>())) ;
                    int newPosition = list.size() - 1;
                    binding.getMessagesAdapter().notifyItemInserted(newPosition);
                    binding.recyclerView.scrollToPosition(newPosition);
                    binding.msgInput.setText("") ;
                    sendMessageSocket(message) ;
                    }else {
                        message =  replaceBadWord(message) ;
                        Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , ChatBoatActivity.this , "Detact offensive words : " + message ,()->{
                            binding.msgInput.setText("") ;
                        }) ;
                    }
                }
            }
        });
    }

    private void sendMessageSocket(String message) {
        String Location ="" ;
        try {
            LocationDetailModel locationDetailModel = new Gson().fromJson(commonDB.getString("LOCATION_MODEL") , LocationDetailModel.class) ;
            Location = locationDetailModel.lat + ":" + locationDetailModel.log ;
        }catch (Exception e){e.printStackTrace();}
        Map<String , String> map = new HashMap<>() ;
        map.put("type" ,"ChatBot") ;
        map.put("Unqid" , loginUserModel.collageUnqid) ;
        map.put("Message" ,message) ;
        map.put("Location" ,Location) ;

        String json = new Gson().toJson(map) ;
        webSocketManager.sendMessage(map , res->{
            RESPONSE = res ;
            runOnUiThread(()->{
                try {
                    RESPONSE = RESPONSE.substring( 1, RESPONSE.length() - 1 ) ;
                    ChatboatModel chatboatModel = new Gson().fromJson(RESPONSE , ChatboatModel.class) ;
                    chatboatModel.setMessageFrom("RECIEVER") ;
                    list.add(chatboatModel) ;
                    int newPosition = list.size() - 1;
                    binding.getMessagesAdapter().notifyItemInserted(newPosition);
                    binding.recyclerView.scrollToPosition(newPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }) ;
    }
    ActivityResultLauncher<Intent> micIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        Intent data = result.getData() ;
                        text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String message = text.get(0);
                        if (!detactBadWord(message)){
                            list.add(new ChatboatModel("1",""+message,"SENT",new ArrayList<>())) ;
                            int newPosition = list.size() - 1;
                            binding.getMessagesAdapter().notifyItemInserted(newPosition);
                            binding.recyclerView.scrollToPosition(newPosition);
                            binding.msgInput.setText("") ;
                            sendMessageSocket(message);
                        }else {
                            message =  replaceBadWord(message) ;
                            Utility.showAnimatedDialog(ANIMATED_DAILOG_TYPE_FAILED , ChatBoatActivity.this , "Detact offensive words : " + message ,()->{
                            }) ;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
    boolean detactBadWord(String msg){
        boolean flag = false ;
        for (int i = 0; i < badWords.size(); i++) {
            String badWord = badWords.get(i);
            if(msg.toLowerCase().contains(badWord)){
                flag = true ;
            }
        }
        return flag ;
    }
    String replaceBadWord(String msg){
        for (int i = 0; i < badWords.size(); i++) {
            String badWord = badWords.get(i);
            if(msg.toLowerCase().contains(badWord)){
                msg = msg.replace(badWord, "*****");
            }
        }
        return  msg ;
    }
}