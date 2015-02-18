/**
 * Juego que consiste en que una changuita elimine cuantos fantasmas pueda, evitando que le pegue
 * el monito de super héroe.
 * 
 *
 * @author Daniela Ramírez Reyes
 * @version 1.0
 * @date 11/02?/2015
 */
 
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;


public class Juego extends Applet implements Runnable, KeyListener {

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private int iDireccion;  //direccion de las flechas
    private int iSpeed;  //velocidad de la nena
    private int iVidas; //vidas del juego
    private int iScore; //contador de puntos
    private int iGhostSpeed;  //velocidad de los fantasmas
    private int iJuanillosSpeed; //velocidad de los juanillos
    private int iContJuanillos; //contador de juanillos
    private boolean bolEnd;  //boleana de final
    private boolean bolPausa;  //boleana de pausa
    private Base basNena;         // Objeto principal
    private Base basFantasmita;         // Objeto malo
    private Base basJuanillo; 
    private LinkedList <Base> lklFantasmas;  //lista de fantasmillas
    private LinkedList <Base> lklJuanillos;  //lista de juanillos
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image imaImagenApplet;   // Imagen a proyectar en Applet	
    private Image imaOver;  
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private AudioClip adcSonidoChimpy;   // Objeto sonido de Chimpy
    private AudioClip adcSonido2; //sonido cuanddo choca con juanillo
	
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     * 
     */
    public void init() {
        // hago el applet de un tamaño 500,500
        setSize(800,500);
        
        iDireccion = 0;
        
        iSpeed = 3;
        
        iVidas = (int) (Math.random() * 2) + 3;
        
        iScore = 0;
             
        iGhostSpeed = (int) (Math.random() * 2) + 3;
        
        iJuanillosSpeed = 1;
        
        URL urlImagenOver= this.getClass().getResource("gameover.jpg"); 
	imaOver = Toolkit.getDefaultToolkit().getImage(urlImagenOver);
        
	URL urlImagenPrincipal = this.getClass().getResource("chimpy.gif");
                
        // se crea el objeto para principal 
	basNena = new Base(0, 0, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));

        // se posiciona a principal  en la esquina superior izquierda del Applet 
        basNena.setX(getWidth() / 2 - basNena.getAncho() / 2);
        basNena.setY(getHeight() - basNena.getAlto());
        
        
        // se crea el objeto para malo 
        int iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO;
        int iPosY = (iMAXALTO - 1) * getHeight() / iMAXALTO;        
	
       
        //creo la lista de fantasmas
        lklFantasmas = new LinkedList();
        
        //se crea una variable random para determinar la cantidad de fantasmas que se pueden
        //agregar al grupito o a la linkedlist
        int iAzar = (int) (Math.random() * 2) + 8;
        
        //se hace un ciclo para ir agregando los fantasmitas respetando el límite del grupo
        for (int iI = 0; iI < iAzar; iI ++) {
            //la posición de x será un número aleatorio con un int negativo para que el fantasma
            //entre desde fuera del applet
            iPosY = -(int) (Math.random() * (getWidth() * 2));   
            //la posición de y será un número aleatorio 
            iPosX = (int) (Math.random() * (getHeight() / 4));  
            
            //se crea el url de la imagen del fantasma
            URL urlImagenFantasmita = this.getClass().getResource("fantasmita.gif");
            // se crea el objeto fantasmita
            basFantasmita = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenFantasmita));
            
            //se genera un numero al azar dentro del rango del alto del applet menos el alto del fantasma
            //para que no se salga
            int iAzarY = (int) (Math.random() * (getHeight() - basFantasmita.getAlto()));
            
            //pongo el fantasma que acabo de crear en la posición al azar que se generó
            basFantasmita.setY(iAzarY);
            
            //agrego los fantasmas a la lista que estaba vacía
            lklFantasmas.add(basFantasmita);
        }
        
        lklJuanillos = new LinkedList();
        
        int iAzar2 = (int) (Math.random() * 5) + 10;
        for (int iI = 0; iI < iAzar2; iI ++) {
            //la posición de x será un número aleatorio con un int negativo para que el juanillo
            //entre desde fuera del applet
            iPosX = (int) (Math.random() * (getWidth()));  
            //la posición de y será un número aleatorio 
            iPosY = (int) (0);   
            
            //se crea el url de la imagen del Juanillo
            URL urlImagenJuanillo = this.getClass().getResource("juanito.gif");
            // se crea el objeto fantasmita
            basJuanillo = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenJuanillo));
            
            //se genera un numero al azar dentro del rango del alto del applet menos el alto del fantasma
            //para que no se salga
            int iAzarY2 = (int) (Math.random() * (getHeight() - basJuanillo.getAlto()));
            
            //pongo el fantasma que acabo de crear en la posición al azar que se generó
            basJuanillo.setY(iAzarY2);
            
            //agrego los fantasmas a la lista que estaba vacía
            lklJuanillos.add(basJuanillo);
        }
        
       
        URL urlSonidoChimpy = this.getClass().getResource("monkey2.wav");
        adcSonidoChimpy = getAudioClip (urlSonidoChimpy);
        
        URL urlSonidoChimpy2 = this.getClass().getResource("monkey1.wav");
        adcSonido2 = getAudioClip (urlSonidoChimpy2);
        
        addKeyListener(this);
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (!bolEnd)   {  //mientras el booleano de juego terminado sea falso
            if(!bolPausa){ //y sólo si la pausa es falsa también
            actualiza();  //se actualiza  y se checa la colisión de las cosas
            checaColision();  
            }
            repaint();//si no de todos modos se usa el repaint
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza(){
        for (Base basFantasmita : lklFantasmas) {   //para cadafantasma
           //establezco que solo se actualizará la posición de x para que avance de lado
            basFantasmita.setX(basFantasmita.getX() + iGhostSpeed); 
        }
        for(Base basJuanillo : lklJuanillos) {
            basJuanillo.setY(basJuanillo.getY() + iJuanillosSpeed);
        }
        
        switch(iDireccion){  //en base a la direccion
            case 1: {    //se mueve hacia arriba
               basNena.setY(basNena.getY() -iSpeed);
               break;
            }
            case 2: {    //se mueve hacia abajo
                basNena.setY(basNena.getY() +iSpeed);
                break;
            }
            case 3: {    //se mueve hacia la izquierda
                basNena.setX(basNena.getX() -iSpeed);
                break;
            }
            case 4: {    //se mueve hacia la derecha
                basNena.setX(basNena.getX() +iSpeed);
                break;
            }
        }
        if(iContJuanillos == 5){
            iContJuanillos = 0;
            iVidas --;
            iJuanillosSpeed ++;
        }
        if(iVidas == 0){
            bolEnd = !bolEnd;
        }
        
        
        

    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        switch(iDireccion){
            case 1: { // si se mueve hacia arriba 
                if(basNena.getY() < 0) { // y esta pasando el limite
                    iDireccion = 2;     // se cambia la direccion para abajo
                }
                break;    	
            }     
            case 2: { // si se mueve hacia abajo
                // y se esta saliendo del applet
                if(basNena.getY() + basNena.getAlto() > getHeight()) {
                    iDireccion = 1;     // se cambia la direccion para arriba
                }
                break;    	
            } 
            case 3: { // si se mueve hacia izquierda 
                if(basNena.getX() < 0) { // y se sale del applet
                    iDireccion = 4;       // se cambia la direccion a la derecha
                }
                break;    	
            }    
            case 4: { // si se mueve hacia derecha 
                // si se esta saliendo del applet
                if(basNena.getX() + basNena.getAncho() > getWidth()) { 
                    iDireccion = 3;       // se cambia direccion a la izquierda
                }
                break;    	
            }			
        }
        for (Base basFantasmita : lklFantasmas) {   //para cada fantasma dentro de la lista
            //checo la colision entre los fantasmas y susanita
            
             //para evitar que los fantasmitallas se salgan del applet de abajo
            if(basFantasmita.getY() + basFantasmita.getAlto() > getHeight()){
                basFantasmita.setY(this.getHeight() - basFantasmita.getAncho());
            }
            //para evitar que los fantasmillas se salgan de arriba
            if(basFantasmita.getY() < 0){
                basFantasmita.setY(0);
            }
            if (basNena.intersecta(basFantasmita)) {  //si se inersecta a susana con el fantasma
                iScore ++;  //si hay colisión se resta 1 punto
                adcSonidoChimpy.play();
                basFantasmita.setX((int) Math.random() * getWidth()); //se reposiciona el fantasma en x = 0
                basFantasmita.setY(-32); //se reposiciona afuera del applet
            }
            //si la imagen del fantasma llega a sobrepasar el ancho del applet
            if(basFantasmita.getX() + basFantasmita.getAncho() > getWidth()) {
                basFantasmita.setX(0); //la x se inicializa en 0
                basFantasmita.setY((int) (Math.random() * getHeight()) -  //l
                        basFantasmita.getAlto());
            }
        }
        for (Base basJuanillo : lklJuanillos) {   //para cada fantasma dentro de la lista
            //checo la colision entre los fantasmas y susanita
            
             //para evitar que los fJuanillos se salgan del applet de la derecha
            if(basJuanillo.getX() + basJuanillo.getAncho() > getWidth()){
                basJuanillo.setX(this.getWidth() - basJuanillo.getAlto());
            }
            //para evitar que los fantasmillas se salgan de la izquierda
            if(basJuanillo.getX() < 0){
                basJuanillo.setX(0);
            }
            if (basNena.intersecta(basJuanillo)) {  //si se inersecta a susana con el el juanete
                adcSonido2.play();
                iContJuanillos ++;
                basJuanillo.setY((int) Math.random() * getWidth()); //se reposiciona el fantasma en x = 0
                basJuanillo.setX(-32); //se reposiciona afuera del applet
            }
            //si la imagen del fantasma llega a sobrepasar el largo del applet
            if(basJuanillo.getY() + basJuanillo.getAlto() > getHeight()) {
                basJuanillo.setY(0); //la x se inicializa en 0
                basJuanillo.setX((int) (Math.random() * getWidth()) -  //l
                        basJuanillo.getAncho());
            }
        }
        

    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void update (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }
        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint(Graphics graDibujo) {
        // si la imagen ya se cargo
        if(!bolEnd){  //si el juego aun continúa
            if (basNena != null && lklFantasmas != null && lklJuanillos != null) {
                    //Dibuja la imagen de principal en el Applet
                    basNena.paint(graDibujo, this);
                
                    // pinto cada fantasma de la lista
                    for (Base basFantasmitas : lklFantasmas) {
                    //Dibuja la imagen de LOS fantasmitas en el Applet
                        basFantasmitas.paint(graDibujo, this);
                    }
                    for (Base basJuanillo : lklJuanillos) {
                    //Dibuja la imagen de LOS fantasmitas en el Applet
                        basJuanillo.paint(graDibujo, this);
                    }
                    //Dibuja la imagen de malo en el Applet
                    basJuanillo.paint(graDibujo, this);
                    graDibujo.setColor(Color.red);  //se establece el color de la letra en rojo
                    graDibujo.drawString("Vidas = " + iVidas, 15, 15);
                    graDibujo.drawString("Puntos = " + iScore, 700,15);
            } // sino se ha cargado se dibuja un mensaje 
            else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
            }
        }else {
                graDibujo.drawImage(imaOver,150,0,this); 
            }  
    }

    @Override
    public void keyTyped(KeyEvent e) {
    
    }

    @Override
    public void keyPressed(KeyEvent e) {
    
    }

    @Override
    public void keyReleased(KeyEvent e) {
     if (e.getKeyCode() == KeyEvent.VK_W) {    //Presiono flecha arriba
            iDireccion = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {    //Presiono flecha abajo
	    iDireccion = 2;
	} else if (e.getKeyCode() == KeyEvent.VK_A) {    //Presiono flecha izquierda
	    iDireccion = 3;
	} else if (e.getKeyCode() == KeyEvent.VK_D) {    //Presiono flecha derecha
	    iDireccion = 4;
        } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){  //si la boleana de esc falsa
            bolEnd = !bolEnd;
        } else if(e.getKeyCode() == KeyEvent.VK_P){  //si la boleana de pausa es falsa
            bolPausa = !bolPausa;          
        }
    }
     
}