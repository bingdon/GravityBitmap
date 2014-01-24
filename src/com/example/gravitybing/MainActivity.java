package com.example.gravitybing;

import java.io.InputStream;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private SensorManager manager;
	private Sensor mSensor;//重力传感器
	
	private static final int TILT_LEFT = 1;
	private static final int TILT_RIGHT = 2;
	private static final int TILT_UP = 4;
	private static final int TILT_DOWN = 8;
	private static final double THRESHOLD = 3.0;
	private Bitmap center,left,
			right,up,down;
	private MainActivity bing;
	private ImageView mImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		manager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mSensor=manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
	
		bing=this;
		
		mImageView=(ImageView)findViewById(R.id.imageView1);
		
		left=readBitMap(bing, R.drawable.left);
		right=readBitMap(bing, R.drawable.right);
		up=readBitMap(bing, R.drawable.up);
		down=readBitMap(bing, R.drawable.down);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		manager.unregisterListener(mGravityListener);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		manager.unregisterListener(mGravityListener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		manager.registerListener(mGravityListener, mSensor, 
				SensorManager.SENSOR_DELAY_NORMAL);
		
	}
	
	private int mLastValue = 0;
	
	SensorEventListener mGravityListener=new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			int value = 0;
			if (event.values[0] < -THRESHOLD) {
				value += TILT_LEFT;
			} else if (event.values[0] > THRESHOLD) {
				value += TILT_RIGHT;
			}
			if (event.values[1] < -THRESHOLD) {
				value += TILT_UP;
			} else if (event.values[1] > THRESHOLD) {
				value += TILT_DOWN;
			}
			if (value != mLastValue) {
				mLastValue = value;
				// send motion command if the tilt changed
				switch (value) {
				case TILT_LEFT:
					
					mImageView.setImageBitmap(right);
//					Toast.makeText(bing, "向左", Toast.LENGTH_LONG).show();
					
					break;
				case TILT_RIGHT:
					mImageView.setImageBitmap(left);
//					Toast.makeText(bing, "向右", Toast.LENGTH_LONG).show();
					
					break;
				case TILT_UP:
					mImageView.setImageBitmap(down);
//					Toast.makeText(bing, "向上", Toast.LENGTH_LONG).show();
					
					break;
				case TILT_DOWN:
					mImageView.setImageBitmap(up);
//					Toast.makeText(bing, "向下", Toast.LENGTH_LONG).show();
					
					break;
				default:
					break;
				}
			}
			
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	 * 
	 * @param context 指针
	 * @param resId 地址id
	 * @return 图片读取
	 */
	public static Bitmap readBitMap(Context context, int resId){  
          BitmapFactory.Options opt = new BitmapFactory.Options();  
          opt.inPreferredConfig = Bitmap.Config.RGB_565;   
          opt.inPurgeable = true;  
          opt.inInputShareable = true;  
          //鑾峰彇璧勬簮鍥剧墖  
          InputStream is = context.getResources().openRawResource(resId);  
          return BitmapFactory.decodeStream(is,null,opt);  
    }

	

}
