package com.example.diss_cbt_application.BreathFeature;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diss_cbt_application.R;

import java.sql.Date;
import java.util.Calendar;

/**This fragment implements the breath feature which runs a simple relaxation and breathing routine.
 **/
public class BreathFragment extends Fragment {

    /*Member variables required throughout the application*/
    private TextView breathTextView, instruction;
    private Button breathButton;
    private long startTime;
    private int count, breathBoolean;


    public BreathFragment() {
        super();
    }


    /**This class is called when the fragment is first created and the view is inflated.
     * Handles the Button on the fragment which starts and stops the exercise.
     *
     * @return - which is the View in which all the other views are placed*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_breath, container, false);

        /*Find Text Views*/
        breathTextView = rootView.findViewById(R.id.tv_breath_number);
        instruction = rootView.findViewById(R.id.tv_breath_instruction);
        breathBoolean = 0;


        breathButton = (Button) rootView.findViewById(R.id.bt_breath_start_stop);

        /*Handles the button click, so starts and stops the timer*/
        breathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();

                if(breathBoolean == 0){
                    startTime = System.currentTimeMillis();;
                    timerHandler.postDelayed(timerRunnable, 0);

                    breathBoolean = 1;
                    breathButton.setText("Stop");

                }
                else{

                    count = 0;
                    timerHandler.removeCallbacks(timerRunnable);
                    breathButton.setText("Start");
                    breathBoolean = 0;

                }
            }
        });

        Log.d("g53mdp", "In Breath Create View");
        return rootView;
    }


    /**Runs a timer on a separate thread which is responsible for timing the exercise.
     * The time is posted back to the main thread every second so that the UI can be updated with new instructions.
     * The handler is responsible for posting these updates back. */
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            breathTextView.setText("" + seconds);
            breathCases(seconds);

            timerHandler.postDelayed(this, 1000);
        }
    };

    /**The exercise is made up of only 13 instructions, many of which repeat. So a modulus is taken
     * of the time to gain a value that is used in a switch case. This in combination with the timer
     * updates the instructions to the user every second.
     *
     * @param - seconds - number of seconds the exercise has been running for. A modulus is taken
     * to gain a value to use in the switch case*/
    private void breathCases(int seconds){

        int value = seconds % 13;

        switch(value){
            case 0:
                instruction.setText("Breath in");
                breathTextView.setText("4");
                break;
            case 1:
            case 8:
                breathTextView.setText("3");
                break;
            case 2:
            case 9:
                breathTextView.setText("2");
                break;
            case 3:
            case 10:
            case 12:
                breathTextView.setText("1");
                break;
            case 4:
            case 11:
                instruction.setText("Hold");
                breathTextView.setText("2");
                break;
            case 5:
                breathTextView.setText("1");
                break;
            case 6:
                instruction.setText("Breath Out");
                breathTextView.setText("5");
                break;
            case 7:
                breathTextView.setText("4");
                break;
        }
    }


}
