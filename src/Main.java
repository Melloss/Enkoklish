import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

class Enkoklish{
    String question;
    ArrayList<String> choices;
    String answer;
    boolean isCorrect;
    String userAnswer;
    Enkoklish(String q,ArrayList<String> c,String a){
        this.question = q;
        this.choices = c;
        this.answer = a;
    }
}
public class Main {
    static ArrayList<Enkoklish> questions = new ArrayList<>();
    static HashMap<String, Integer> score = new HashMap<String, Integer>();
    static int questionNumber = 0;
    static int time = 15;
    static int resultCount = 0;
    public Clip tickingClip;


    public static ArrayList<String> separateChoices(String data) {
        StringTokenizer tokenizer = new StringTokenizer(data, " ");
        ArrayList<String> cs = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
            cs.add(tokenizer.nextToken());
        }
        Collections.shuffle(cs);
        return cs;
    }

    static public void getEverythingReady() {
        score.put("correct", 0);
        try {
            File file = new File("./src/resource/question.txt");
            Scanner sc = new Scanner(file);
            int  i = 1;
            while (sc.hasNextLine()) {
                if(i%3 == 0){
                    Enkoklish enkoklish = new Enkoklish(sc.nextLine(), separateChoices(sc.nextLine()),sc.nextLine());
                    questions.add(enkoklish);
                }
                i++;
            }
            Collections.shuffle(questions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        getEverythingReady();
        Main main = new Main();
        main.new Question();
    }
    public class TickingClock extends JPanel {


        public TickingClock() {
            try {
                File audioFile = new File("./src/resource/tick.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                tickingClip = AudioSystem.getClip();
                tickingClip.open(audioStream);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
                tickingClip.setFramePosition(0);
                tickingClip.start();
            });
            timer.start();
        }


    }

    public class Question extends javax.swing.JFrame {


    public Question() {
        initComponents();
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        jScrollPane1.setBorder(null);
        choiceA.setFocusPainted(false);
        choiceB.setFocusPainted(false);
        choiceC.setFocusPainted(false);
        choiceD.setFocusPainted(false);
        setLocationRelativeTo(null);
        add(new TickingClock());
        setResizable(false);
        setTitle("Enkoklish");
        setVisible(true);
        question_display.setText(questions.get(questionNumber).question);
        choiceA.setText(questions.get(questionNumber).choices.get(0));
        choiceB.setText(questions.get(questionNumber).choices.get(1));
        choiceC.setText(questions.get(questionNumber).choices.get(2));
        choiceD.setText(questions.get(questionNumber).choices.get(3));
        current_question.setText(questionNumber+1 + "/" + (questions.size()));
        questionNumber++;
        timer.setText(time+"");
        startTimer();
    }
    private void startTimer(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.setText(time+"");
                time--;
                if(time == -1){
                    t.cancel();
                    nextQuestion(null);
                }
            }
        },0,1000);
    }
    private void nextQuestion(JButton choiceSelected) {
        if(choiceSelected == null){
            questions.get(questionNumber-1).userAnswer = "<b>Not Answered</b>";
            questions.get(questionNumber-1).isCorrect = false;
            if (questionNumber >= questions.size()) {
                tickingClip.close();
                if(resultCount == 0){
                    dispose();
                    Main main = new Main();
                    main.new Result();
                    resultCount++;
                }

            }
            else {
                question_display.setText(questions.get(questionNumber).question);
                choiceA.setText(questions.get(questionNumber).choices.get(0));
                choiceB.setText(questions.get(questionNumber).choices.get(1));
                choiceC.setText(questions.get(questionNumber).choices.get(2));
                choiceD.setText(questions.get(questionNumber).choices.get(3));
                current_question.setText(questionNumber+1 + "/" + (questions.size()));
                time = 15;
                timer.setText(time+"");
                questionNumber++;
                startTimer();
            }


        }
        else{
            time = 15;
            questions.get(questionNumber-1).userAnswer = choiceSelected.getText();
            if (questionNumber >= questions.size()) {
                tickingClip.close();
                if (choiceSelected.getText().equalsIgnoreCase(questions.get(questionNumber-1).answer)) {
                    score.put("correct", (score.get("correct") + 1));
                    questions.get(questionNumber-1).isCorrect = true;
                }
                else {
                    questions.get(questionNumber-1).isCorrect = false;
                }
                if(resultCount == 0){
                    dispose();
                    Main main = new Main();
                    main.new Result();
                    resultCount++;
                }
            } else {
                current_question.setText(questionNumber+1 + "/" + (questions.size()));
                if (choiceSelected.getText().equalsIgnoreCase(questions.get(questionNumber-1).answer)) {
                    score.put("correct", (score.get("correct") + 1));
                    questions.get(questionNumber-1).isCorrect = true;
                }
                else  {
                    questions.get(questionNumber-1).isCorrect = false;
                }
                question_display.setText(questions.get(questionNumber).question);
                choiceA.setText(questions.get(questionNumber).choices.get(0));
                choiceB.setText(questions.get(questionNumber).choices.get(1));
                choiceC.setText(questions.get(questionNumber).choices.get(2));
                choiceD.setText(questions.get(questionNumber).choices.get(3));
                questionNumber++;
            }
        }


    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        choiceC = new javax.swing.JButton();
        choiceB = new javax.swing.JButton();
        choiceA = new javax.swing.JButton();
        choiceD = new javax.swing.JButton();
        timer = new javax.swing.JLabel();
        current_question = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        question_display = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        jPanel2.setMinimumSize(new java.awt.Dimension(733, 482));
        jPanel2.setPreferredSize(new java.awt.Dimension(733, 482));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        choiceC.setBackground(new java.awt.Color(255, 51, 51));
        choiceC.setFont(new java.awt.Font("Lohit Devanagari", 1, 16)); // NOI18N
        choiceC.setForeground(new java.awt.Color(255, 255, 255));
        choiceC.setText("Choice C");
        choiceC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choiceCActionPerformed(evt);
            }
        });
        jPanel2.add(choiceC, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, 290, 40));

        choiceB.setBackground(new java.awt.Color(0, 153, 0));
        choiceB.setFont(new java.awt.Font("Lohit Devanagari", 1, 16)); // NOI18N
        choiceB.setForeground(new java.awt.Color(255, 255, 255));
        choiceB.setText("Choice B");
        choiceB.setName(""); // NOI18N
        choiceB.setPreferredSize(new java.awt.Dimension(94, 28));
        choiceB.setRequestFocusEnabled(false);
        choiceB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choiceBActionPerformed(evt);
            }
        });
        jPanel2.add(choiceB, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 263, 290, 40));

        choiceA.setBackground(new java.awt.Color(0, 204, 204));
        choiceA.setFont(new java.awt.Font("Lohit Devanagari", 1, 16)); // NOI18N
        choiceA.setForeground(new java.awt.Color(255, 255, 255));
        choiceA.setText("Choice A");
        choiceA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choiceAActionPerformed(evt);
            }
        });
        jPanel2.add(choiceA, new org.netbeans.lib.awtextra.AbsoluteConstraints(63, 264, 290, 40));

        choiceD.setBackground(new java.awt.Color(102, 102, 0));
        choiceD.setFont(new java.awt.Font("Lohit Devanagari", 1, 16)); // NOI18N
        choiceD.setForeground(new java.awt.Color(255, 255, 255));
        choiceD.setText("Choice D");
        choiceD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choiceDActionPerformed(evt);
            }
        });
        jPanel2.add(choiceD, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 350, 290, 40));

        timer.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        timer.setForeground(new java.awt.Color(255, 255, 255));
        timer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timer.setText("15 sec");
        jPanel2.add(timer, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, 130, 40));

        current_question.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        current_question.setForeground(new java.awt.Color(255, 255, 255));
        current_question.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        current_question.setText("1/10");
        jPanel2.add(current_question, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 130, 40));

        jScrollPane1.setName(""); // NOI18N
        jScrollPane1.setOpaque(false);

        question_display.setFont(new java.awt.Font("Liberation Sans", 1, 17)); // NOI18N
        question_display.setForeground(new java.awt.Color(255, 255, 255));
        question_display.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        question_display.setText("Question ");
        jScrollPane1.setViewportView(question_display);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 400, 110));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("./resource/question_board.png"))); // NOI18N
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void choiceAActionPerformed(java.awt.event.ActionEvent evt) {
        nextQuestion(choiceA);
    }

    private void choiceBActionPerformed(java.awt.event.ActionEvent evt) {
        nextQuestion(choiceB);
    }

    private void choiceCActionPerformed(java.awt.event.ActionEvent evt) {
        nextQuestion(choiceC);
    }

    private void choiceDActionPerformed(java.awt.event.ActionEvent evt) {
        nextQuestion(choiceD);
    }


    // Variables declaration - do not modify
    private javax.swing.JButton choiceA;
    private javax.swing.JButton choiceB;
    private javax.swing.JButton choiceC;
    private javax.swing.JButton choiceD;
    private javax.swing.JLabel current_question;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel question_display;
    private javax.swing.JLabel timer;
}

    public class Result extends javax.swing.JFrame {

        public Result() {
            initComponents();
            jScrollPane1.setOpaque(false);
            jScrollPane1.getViewport().setOpaque(false);
            jScrollPane1.setBorder(null);
            setLocationRelativeTo(null);
            setTitle("Enkoklish Result");
            setResizable(false);
            setVisible(true);
            status.setText("You Answer " + score.get("correct") + "/" + (questions.size()));
            if(score.get("correct") == questions.size()){
                result_display.setText("You are Amazing!");
            }
            else if(score.get("correct") == 0){
                result_display.setText("You Answer Nothing!");
            }
            else{
                String html = "<html><body><p>" ;
                for (int i=0;i< questions.size();i++){
                    if(questions.get(i).isCorrect == false){
                        html = html.concat(questions.get(i).question+
                                "<br>You Answer: "+questions.get(i).userAnswer+
                                "<br>Correct Answer: <b>"+questions.get(i).answer+
                                "</b><br><br>");
                    }

                }
                html = html.concat("</p></body></html>");
                result_display.setText(html);
            }
        }
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

            jPanel1 = new javax.swing.JPanel();
            status = new javax.swing.JLabel();
            jScrollPane1 = new javax.swing.JScrollPane();
            result_display = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

            jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            status.setFont(new java.awt.Font("Liberation Sans", 1, 22)); // NOI18N
            status.setForeground(new java.awt.Color(255, 255, 255));
            status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            status.setText("You Answer 1/3");
            jPanel1.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 230, 60));

            jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));
            jScrollPane1.setOpaque(false);

            result_display.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
            result_display.setForeground(new java.awt.Color(255, 255, 255));
            result_display.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            result_display.setText("Result");
            result_display.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            jScrollPane1.setViewportView(result_display);

            jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 620, 220));

            jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("./resource/result.png"))); // NOI18N
            jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 360));

            getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

            pack();
        }// </editor-fold>

        // Variables declaration - do not modify
        private javax.swing.JLabel jLabel2;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JLabel result_display;
        private javax.swing.JLabel status;
        // End of variables declaration
    }
}




