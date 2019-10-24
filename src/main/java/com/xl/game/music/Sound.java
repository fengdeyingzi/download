package com.xl.game.music;





public class Sound
	{
		//音乐类型
		private int format;
		public static final int 
		FORMAT_TONE = 1,
		FORMAT_WAV = 5;
		
		//音乐播放状态
		private int soundtype;
		public static final int 
		SOUND_PLAYING = 0,
		SOUND_STOPPED = 1,
		SOUND_UNINITIALIZED = 3;
		
		

		public Sound(int r2_int,long r3_long)
			{
				this.soundtype=3;
				this.format=0;
			}

		public Sound(byte[] r2_byte_A,int id)
			{
				this.soundtype=3;
				this.format=0;
			}

		public static int getConcurrentSoundCount(int r1_int)
			{
				return 1;
			}

		public static int[] getSupportedFormats()
			{
				return new int[] { 1, 5 };
			}

		public int getGain()
			{
				return this.format;
			}

		public int getState()
			{
				return this.soundtype;
			}

		public void init(byte[] r2_byte_A,int id)
			{
				this.soundtype=3;
			}
			
		public void init(String filename,int id)
		{
			
		}

		public void play(int id)
			{
				this.soundtype=0;
			}

		public void release()
			{
				this.soundtype=3;
			}

		public void resume()
			{
				this.soundtype=0;
			}

		public void setGain(int r1_int)
			{
				this.format=r1_int;
			}

		public void setSoundListener(SoundListener Listener)
			{
			}

		public void stop()
			{
				this.soundtype=1;
			}
	}

