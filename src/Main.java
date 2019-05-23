import org.osbot.rs07.accessor.XNPC;
import org.osbot.rs07.api.model.Character;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.util.concurrent.TimeUnit;
import java.awt.*;

@ScriptManifest(name = "Bird nest crusher", author = "dokato", version = 1.5, info = "", logo = "") 
public class Main extends Script {
	
	private static final Color standardTxtColor = new Color(255, 255, 255);
	
	private static final int CRUSHED_NEST_ID = 6693;
	private static final int BIRD_NEST_ID = 5075;
	private static final int PESTLE_AND_MORTAR_ID = 233;
	
	private int nestsInBank;
	
	private boolean startb = true;
	
    private long timeRan;
    private long timeBegan;
	
	private String status;
	@Override
    public void onStart(){
		this.timeBegan = System.currentTimeMillis();
		nestsInBank = 0;
    }

	@Override
    public void onExit() {
    }


    @Override
    public int onLoop() throws InterruptedException{
    	status="loop started";
    	procedures();    	
    	
    	if(hasBirdsNestsInInv()){
    		if(hasPestleAndMortarInInv())
    			crushNests();
    		else
    			stop();
    	}else{
    		bank();
    	}
    	
    	status="loop ended";
    	return random(16,39);
    }

    private void bank() throws InterruptedException {
    	status="checking if bank is open";
    	if(getBank().isOpen()){
    		status="checking if has pestle and mortar in inv";
	    	if(!hasPestleAndMortarInInv()){
	    		status="checking is bank has pestel and mortar in it";
	    		if(getBank().contains(PESTLE_AND_MORTAR_ID)){
	    			status="withdrawing pestle and mortar";
	    			getBank().withdraw(PESTLE_AND_MORTAR_ID, 1);
	    			sleep(random(274,897));
	    		}else{
	    			status="no pestle and mortar, stopping";
	    			stop();
	    		}
	    	}else{
	    		status="checking if has bird nest in inv";
	    		if(!getInventory().contains(BIRD_NEST_ID)){
	    			status="checking if inventory is full";
	    			if(getInventory().isFull()){
	    				status="depositing all crushed nests";
	    				getBank().depositAll();
	    				sleep(random(274,897));
	    			}
	    			status="checking if has crushed nests in bank";
	    			if(getBank().contains(CRUSHED_NEST_ID)){
	    				status="getting amount of nests";
		    			nestsInBank = getBank().getItem(CRUSHED_NEST_ID).getAmount();
	    			}
	    			status="checking if has bird nest in bank";
	    			if(getBank().contains(BIRD_NEST_ID)){
	    				status="withdrawing bird nests";
	    				getBank().withdrawAll(BIRD_NEST_ID);
	        			sleep(random(274,897));
	    			}else{
	    				status="no bird nests in bank, stopping";
	    				stop();
	    			}
	    		}
	    	}
    	}else{
    		status="opening bank";
    		getBank().open();
    	}
	}

	private void crushNests() throws InterruptedException {
		status="whecking if bank is open to crush nests";
		if(getBank().isOpen()){
			status="closing bank";
			getBank().close();
		}
		
		if(getInventory().isFull()){
		status="checking if item is selected";
			if(getInventory().isItemSelected()){
				getMouse().click(getInventory().getMouseDestination(27));
			}else{
				getMouse().click(getInventory().getMouseDestination(26));
			}
		}else{
			if(getInventory().isItemSelected()
					&& getInventory().getSelectedItemName().equals(getInventory().getItem(BIRD_NEST_ID).getName())){
					getInventory().getItem(PESTLE_AND_MORTAR_ID).interact("Use");
			}else{
				getInventory().getItem(BIRD_NEST_ID).interact("Use");
			}
		}
	}

	private boolean hasPestleAndMortarInInv() {
		status="checking if has p&e in inv";
		return getInventory().contains(PESTLE_AND_MORTAR_ID);
	}

	private boolean hasBirdsNestsInInv() {
		status="checking if has bird nest in inv";
		return getInventory().contains(BIRD_NEST_ID);
	}

	private void procedures() {
    	if(getCamera().getYawAngle() == 0)
    		getCamera().moveYaw(random(200,330));
    	if(getCamera().getPitchAngle() > 55)
    		getCamera().movePitch(random(21,48));
	}

	private void deselectItem() throws InterruptedException {
		if(getInventory().isItemSelected()){
			getInventory().deselectItem();
			sleep(random(54,123));
		}
	}

	@Override
    public void onPaint(Graphics2D g1){
    	
    	if(this.startb){
    		this.startb=false;
    		this.timeBegan=System.currentTimeMillis();
    	}
    	this.timeRan = (System.currentTimeMillis() - this.timeBegan);
		
		Graphics2D g = g1;

		int startY = 120;
		int increment = 15;
		int value = (-increment);
		int x = 20;
		
		g.setFont(new Font("Arial", 0, 13));
		g.setColor(standardTxtColor);
		g.drawString("Acc: " + getBot().getUsername().substring(0, getBot().getUsername().indexOf('@')), x,getY(startY, value+=increment));
		g.drawString("World: " + getWorlds().getCurrentWorld(),x,getY(startY, value+=increment));
		value+=increment;
		g.drawString("Version: " + getVersion(), x, getY(startY, value+=increment));
		g.drawString("Runtime: " + ft(this.timeRan), x, getY(startY, value+=increment));
		g.drawString("Status: " + status, x, getY(startY, value+=increment));
		value+=increment;
		g.drawString("Crushed in bank: " + nestsInBank, x, getY(startY, value+=increment));	
    }
    

	private int getY(int startY, int value){
		return startY + value;
	}
    
    private void fillRect(Graphics2D g, Rectangle rect){
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
	}
    
    public void onMessage(Message message) throws InterruptedException {
    }
    
	private String ft(long duration) {
		String res = "";
		long days = TimeUnit.MILLISECONDS.toDays(duration);
		long hours = TimeUnit.MILLISECONDS.toHours(duration)
				- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
						.toHours(duration));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
						.toMinutes(duration));
		if (days == 0L) {
			res = hours + ":" + minutes + ":" + seconds;
		} else {
			res = days + ":" + hours + ":" + minutes + ":" + seconds;
		}
		return res;
	}
}