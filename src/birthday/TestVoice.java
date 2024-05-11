package birthday;
import com.sun.speech.freetts.Gender;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.JavaClipAudioPlayer;
public class TestVoice {


        String voiceName = "kevin16";
        VoiceManager voiceManager;
        Voice voice;

        TestVoice(String name){
            voiceName = name;
            this.setup();
        }

        void listAllVoices() {
            System.out.println();
            System.out.println("All voices available:");
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            VoiceManager voiceManager = VoiceManager.getInstance();
            Voice[] voices = voiceManager.getVoices();
            for (int i = 0; i < voices.length; i++) {
                System.out.println("    " + voices[i].getName()
                        + " (" + voices[i].getDomain() + " domain)");
            }
        }

        void setup() {
            listAllVoices();
            System.out.println();
            System.out.println("Using voice: " + voiceName);

            voiceManager = VoiceManager.getInstance();
            voice = voiceManager.getVoice(voiceName);

            voice.setPitch(50.75F);
            voice.setPitchShift(2.25F);
            voice.setDurationStretch(1.00F);
            voice.setRate(120.00F);
            // voice.setPitchRange(10.1); //mutace
            voice.setStyle("breathy");  //"business", "casual", "robotic", "breathy"

            if (voice == null) {
                System.err.println(
                        "Cannot find a voice named "
                                + voiceName + ".  Please specify a different voice.");
                System.exit(1);
            }
            voice.allocate();
        }

        public void mluv(String _a){

            if(_a==null){
                _a= "nothing";
            }
            voice.speak(_a);

        }

        public void exit(){
            voice.deallocate();
        }

}
