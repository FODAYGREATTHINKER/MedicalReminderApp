package com.anwesome.app.medicalpillreminder.views;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Toast;

import com.anwesome.app.medicalpillreminder.RealmModelUtil;
import com.anwesome.app.medicalpillreminder.ReminderUtil;
import com.anwesome.app.medicalpillreminder.models.Pill;

/**
 * Created by anweshmishra on 17/02/17.
 */
public class ReminderLayout extends RefillLayout {

    private Activity activity;
    private RealmModelUtil realmModelUtil;
    public ReminderLayout(final Context context, AttributeSet attrs) {
        super(context,attrs);
        realmModelUtil = new RealmModelUtil(context);
        activity = (Activity)context;
    }
    public ReminderLayout(final Context context) {
        super(context);
        realmModelUtil = new RealmModelUtil(context);
        activity = (Activity)context;

    }
    public boolean plusViewRequired() {
        return false;
    }
    public RefillView getView(Context context,Pill pill) {
        return new ReminderView(context,pill);
    }
    public class ReminderView extends RefillView {
        private Pill pill;
        private Icon plus;
        private SchedulerView schedulerView;
        public ReminderView(final Context context, final Pill pill) {
            super(context,pill);
            this.pill = pill;
            schedulerView = new SchedulerView(context, new SubmitListener() {
                @Override
                public void onSubmit(String hour,String minute,String period) {
                    Toast.makeText(context,hour+":"+minute+":"+period,Toast.LENGTH_LONG).show();
                    realmModelUtil.addNotification(pill.getId(),hour+":"+minute+" "+period);
                    ReminderUtil.createReminderForPill(activity,pill,hour,minute,period);
                    activity.setContentView(new ReminderLayout(context));
                }
            });
        }
        public void init(int w,int h) {
            plus = new Icon(9*w/10,2*h/3,w/20){
                public void draw(Canvas c,Paint p) {
                    super.draw(c,p);
                    float gap = (this.r*4)/5;
                    c.drawLine(this.x-gap,this.y,this.x+gap,this.y,p);
                    c.drawLine(this.x,this.y-gap,this.x,this.y+gap,p);

                }
                public void handleRealm() {
                    activity.setContentView(schedulerView);
                }
            };
        }
        public void drawControls(Canvas canvas, Paint paint,int w,int h) {
            String notificationTimes = pill.getNotificationTimes()==null?" ":pill.getNotificationTimes();
            canvas.drawText(notificationTimes,w/10,2*h/3,paint);
            plus.draw(canvas,paint);
        }
        public void handleTap(float x,float y) {
            plus.handleTap(x,y);
        }
    }
}
