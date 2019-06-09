package aramaMotoru;
//kütüphaneler....
import java.awt.Font;
import java.awt.PageAttributes;
import static java.awt.PageAttributes.MediaType.A;
import java.io.BufferedReader;
import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.nio.file.Files.size;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.text.Style;
//dinamik değişkenlerimi tanımlıyorum...
public class aramaMotoru extends javax.swing.JFrame {
    int i=0,N=100,key=211,x=1,index=0,ascii=0,mod,y=0,e,u;
    String[][] hash = new String[key][4];
    String[] veri = new String[N];
    String[] eksik=new String[100];
    String[] yerdeg=new String[100];
    String eksikKelime;
    String kelime;
    String degisik;
//JList için kullanıyoruz...
    DefaultListModel<String> model ;
//Veri aktarma fonksiyonunu kullanarak txt dosyasındaki  verileri okuma işlemini yapıyorum...
    public void veriaktarma() throws FileNotFoundException, IOException { 
// dosya okuma işlemi yapılıyor....
        File f = new File("D:\\cü\\MÜHENDİSLİK PROJESİ\\mühendislik projesi-1\\2015123019_Busra_DEMIRTAS\\hash.txt");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String satir = br.readLine();
        while(satir != null){
//küçüğe çevirilip atıyor
            veri[i] = satir.toLowerCase(); i++; 
            satir=br.readLine();
        }
        br.close();        
        for(int k=0; k < key; k++){
//String.valueOf = tip dönüşümü için kullanılıyor...
           hash[k][0]=String.valueOf(k);
           hash[k][1]="";
           hash[k][2]="";
           hash[k][3]="";
        }
//txt dosyasından okuduğumuz verileri kelime nesnesine atıyorum....        
            for(int k=0; k < 100; k++){            
                ascii=0;
                kelime=veri[k];
                x=1;
//ascii karşılığını hesaplıyor...
                for(int n = 0; n < kelime.length(); n++){
                    ascii += ((n+1) * kelime.charAt(n));
                }
//modunu hesaplıyor....
                index = ascii % key;
                mod=index;
//ağırlandırma işlemi hesaplıyor...
                while(hash[index][2] != ""){
                    index=mod;
                    index = (index +(x*x)) % key;
                    x++;
                }
//yapılan hesaplamaları hash mantığına göre diziye atıyorum....    
                hash[index][0]=String.valueOf(index);
                hash[index][1]=String.valueOf(ascii);
                hash[index][2]=kelime;
                hash[index][3]=String.valueOf(mod);            
                ascii=0;
            }
            DefaultListModel dlm = new DefaultListModel();
//JListe yazılım gerçekleşiyor...            
            dlm.addElement("indis"+" -- "+"ascii"+" -- "+"kelime"+" -- "+"mod");
                for(int ii= 0; ii <key ; ii++){
                    dlm.addElement(hash[ii][0] + ".indis  "+ hash[ii][1]+"    "+hash[ii][2]+"   "+hash[ii][3]);
                }
            jList1.setModel(dlm);
    }
//girdiğim kelimenin tabloda olup olmadığını kontrol eden fonksiyon....
    public void arama1(){
//kelime küçük harflere çevriliyor..        
        String kelime=text1.getText().toLowerCase(); 
        y=0;
        ascii=0;
        x=0;
//keliemnin ascii hesaplanıyor...        
       for(int i=0;i<kelime.length();i++){
           ascii=ascii+(i+1)*kelime.charAt(i);
       }
//modu alınıyor...       
       index= ascii % key;  
       mod=index;
//hesaplanan ascii karşılaştırılıyor eşit ise kelimeyi indisinde buluyor. eşit değilse diğer indislerde olup olmadığına bakılıyor....       
       do{           
           if(hash[index][2].equals(kelime)){
               y++;               
           }
           else{
               index=mod;
               index= (index+(x*x)) % key;
               x++;
       }               
       }while(y<=0 && x<=211);
//bulunan kelimenin uyarı mesajı....       
       if(y > 0)
           JOptionPane.showMessageDialog(null, kelime+" kelimesi bulunmuştur.");  
    }
//aranan kelimenin harflerini sırayla silerek işlem yapan fonksiyon....
    public void arama2() {
        int ii=0;u=0;
        String kelime=text1.getText().toLowerCase();
        StringBuilder sb = new StringBuilder();
        for(int ix=0;ix<eksik.length;ix++){
            eksik[ix]=null;
        }
//karakterlerin tek tek silinmesi....     
        for(int i=0; i<kelime.length();i++){                       
            sb.delete(0, sb.length());
            sb.append(kelime);
            sb.deleteCharAt(i);
            eksikKelime=sb.toString();            
            ascii=0;
            y=0;
            x=0;
//oluşan kelimenin ascii modu hesaplanıyor....            
            for(int j=0;j<eksikKelime.length();j++){
                ascii=ascii+(j+1)*eksikKelime.charAt(j);
            }            
            index= ascii % key;  
            mod=index;
//kelimeler karşılaştırılıyor           
            do{           
                if(hash[index][2].equals(eksikKelime)){
                    y++;               
                }
                else{
                    index=mod;
                    index= (index+(x*x)) % key;
                    x++;
                }                            
            } while(y<=0 && x<=211);
            if(y > 0){
            eksik[ii]=eksikKelime;
            ii++;
            }
        }
// benzer kelime bulunduysa uyarı mesajı....
        for (String eksik1 : eksik) {
            if (eksik1 != null) {
                u++;
            }
        }
        for(int iii=0;iii<u;iii++){
            JOptionPane.showMessageDialog(null, kelime+" kelimesi yoktur. Benzeri olan "+eksik[iii]+" kelimesi vardır.");             
        }
    }
//aranan kelimenin komşu harfleriyle yer değiştiren fonksiyon....
    public void arama3(){       
        e=0;int iiii=0;
        ArrayList harf=new ArrayList();
        String kelime=text1.getText().toLowerCase();  
        for(int ix=0;ix<yerdeg.length;ix++){
            yerdeg[ix]=null;
        }
        for(int iii=0;iii<kelime.length()-1;iii++){
            harf.clear();
            degisik="";
            
            for(int ii=0;ii<kelime.length();ii++){
            harf.add(kelime.charAt(ii));
        }
//karakterlerin yerleri değişiyor..            
            Object temp=harf.get(iii);
            harf.set(iii, harf.get(iii+1));
            harf.set(iii+1, temp);
            for(int i1=0; i1<harf.size();i1++){                
                degisik += harf.get(i1);                
            }
            ascii=0;
            y=0;
            x=0;
//ascii mod hesaplanıyor....            
            for(int a=0; a<degisik.length();a++){
                ascii=ascii+(a+1)*degisik.charAt(a);
            }
            index=ascii % key;
            mod=index;
//kelimeler karşılaştırılıyor...            
            do{           
                if(hash[index][2].equals(degisik)){
                    y++;               
                }
                else{
                    index=mod;
                    index= (index+(x*x)) % key;
                    x++;
                }
            }while(y<=0 && x<=211);
                if(y>0){
                    yerdeg[iiii]=degisik;
                    iiii++;
                }
            }
//benzer kelime bulunduysa uyarı mesajı.....        
            for(int f=0;f<yerdeg.length;f++){
                if(yerdeg[f] != null){
                    e++;
                }
            }
            for(int g=0;g<e;g++){
                JOptionPane.showMessageDialog(null, kelime+" kelimesi yoktur. Benzeri olan "+yerdeg[g]+" kelimesi vardır." );
            }
        }      
    public aramaMotoru() {
        super("ARAMA MOTORU");        
        initComponents();        
        try {
            veriaktarma();
        } catch (IOException ex) {
            Logger.getLogger(aramaMotoru.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        text1 = new javax.swing.JTextField();
        btn1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        text1.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        text1.setForeground(new java.awt.Color(204, 51, 0));
        text1.setText("Aranacak Kelimeyi Giriniz..");
        text1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        text1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        text1.setMinimumSize(new java.awt.Dimension(3, 20));
        text1.setName(""); // NOI18N
        text1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                text1MouseClicked(evt);
            }
        });
        text1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text1ActionPerformed(evt);
            }
        });

        btn1.setBackground(new java.awt.Color(255, 255, 255));
        btn1.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        btn1.setForeground(new java.awt.Color(204, 0, 0));
        btn1.setText("ARA");
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });

        jList1.setBackground(new java.awt.Color(204, 51, 0));
        jList1.setForeground(new java.awt.Color(255, 255, 255));
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jList1.setMinimumSize(new java.awt.Dimension(10, 20));
        jList1.setVisibleRowCount(211);
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(text1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
                .addContainerGap(133, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(164, 164, 164))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(text1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn1)
                .addGap(33, 33, 33))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        arama1();
        if(y==0){
            arama2();
            arama3();
            if(e == 0 && u ==0){
                JOptionPane.showMessageDialog(null,"Aradığınız kelime bulunamadı.");
            }
        }
    }//GEN-LAST:event_btn1ActionPerformed
    private void text1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text1ActionPerformed
      
    }//GEN-LAST:event_text1ActionPerformed
    private void text1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_text1MouseClicked
       //mouse olayı 
        text1.setText("");
    }//GEN-LAST:event_text1MouseClicked
    public static void  main(String args[]) throws FileNotFoundException, IOException {       
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new aramaMotoru().setVisible(true);
            }            
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField text1;
    // End of variables declaration//GEN-END:variables
}