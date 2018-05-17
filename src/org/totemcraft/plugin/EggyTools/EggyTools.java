package org.totemcraft.plugin.EggyTools;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EggyTools extends JavaPlugin implements Listener {
    private EditSession es;
    private Selection s;
    public Player player;
    public World world;




    @Override
    public void onEnable(){
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        String msg = "「EggyTools已加载」";
        this.getLogger().info(msg);


    }

    @Override
    public void onDisable(){

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED+"别用console执行");
        }
        Player p = (Player)sender;


        if (command.getName().equalsIgnoreCase("eggy")){
            WorldEditPlugin main = getwe();
            WorldEdit we = main.getWorldEdit();
            world = ((Player) sender).getWorld();
            es = getwe().createEditSession(p);
            s = getwe().getSelection(p);
            if (s == null){
                sender.sendMessage(ChatColor.RED+"你啥都没选中");
                return true;
            }



        }


        if (command.getName().equalsIgnoreCase("egg")){

            world = ((Player) sender).getWorld();

            s = getwe().getSelection(p);
            if (s == null){
                sender.sendMessage(ChatColor.RED+"你啥都没选中");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN+"遍历区域已创建在 "+"pos1:"+s.getMinimumPoint().getBlockX()+","+s.getMinimumPoint().getBlockY()+","+s.getMinimumPoint().getBlockZ()+" pos2:"+s.getMaximumPoint().getBlockX()+","+s.getMaximumPoint().getBlockY()+","+s.getMaximumPoint().getBlockZ());
            int minx = s.getMinimumPoint().getBlockX();
            int miny = s.getMinimumPoint().getBlockY();
            int minz = s.getMinimumPoint().getBlockZ();
            int maxx = s.getMaximumPoint().getBlockX();
            int maxy = s.getMaximumPoint().getBlockY();
            int maxz = s.getMaximumPoint().getBlockZ();
            sender.sendMessage(ChatColor.BLUE+"开始遍历。。。");
            List<org.bukkit.util.Vector> cmdpos = new ArrayList<>();
            List<String> cmd = new ArrayList<>();

            for (int mx = minx; mx < maxx; mx++){
                for (int my = miny; my < maxy; my++){
                    for (int mz = minz; mz < maxz; mz++){
                        int flag = checkBlock(mx,my,mz);
                        if (flag == 1 || flag == 2){
                            cmdpos.add(new org.bukkit.util.Vector(mx,my,mz));
                        }

                    }
                }
            }

            sender.sendMessage(ChatColor.GREEN+"找到"+cmdpos.size()+"个命令方块或命令方块矿车,准备处理包含指令");

            for (int i = 0; i < cmdpos.size(); i++){
                int x = cmdpos.get(i).getBlockX();
                int y = cmdpos.get(i).getBlockY();
                int z = cmdpos.get(i).getBlockZ();
                Block b = world.getBlockAt(x,y,z);
                cmd.add(getCommandBlockCommand(b));
            }

            try {
                File f = new File("cmdList.txt");
                if (!f.exists()){f.createNewFile();}
                BufferedWriter bw= new BufferedWriter(new FileWriter("cmdList.txt",true));
                for (int i = 0; i < cmd.size(); i++){
                    bw.write(cmd.get(i));
                    bw.newLine();
                }
                bw.flush();
                bw.close();
                sender.sendMessage(ChatColor.GREEN+"写入完成");
            }
            catch (Exception e){
                System.out.print(e.getMessage());
            }


            try {
                addScoreName(sender);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;

    }

    public void tellme(Object msg){
        Bukkit.broadcastMessage((String) msg);
    }


    public void addScoreName(CommandSender sender) throws IOException{
        String pattern = "(?<=scoreboard objectives setdisplay sidebar ).+?.*";
        String pattern2 = "(?<=score_).+?(?=_)";
        String pattern3 = "(?<=score_).+?(?=(=))";
        String zdType = "(?<=scoreboard players test|set|add|remove ).+(?= )";
        String zdSlove = "(?<=] ).*";
        Pattern r = Pattern.compile(pattern);
        Pattern r3 = Pattern.compile(pattern3);
        Pattern r2 = Pattern.compile(pattern2);
        Pattern z1 = Pattern.compile(zdType);
        Pattern z2 = Pattern.compile(zdSlove);
        FileReader fr = new FileReader("cmdList.txt");
        BufferedReader bfread = new BufferedReader(fr);
        String line = null;
        String cmd = null;
        String cmd2 = null;
        String cmd3 = null;
        String cmd4 = null;
        while ((line = bfread.readLine()) != null){
            Matcher m3 = r3.matcher(line);
            Matcher zz1 = z1.matcher(line);
            Matcher m2 = r2.matcher(line);
            Matcher m = r.matcher(line);
            if (m.find()){
                String val = m.group();
                cmd = "scoreboard objectives add "+val+" dummy "+val;
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
                sender.sendMessage(ChatColor.GOLD+"写入一条记分板");
            }
            else if (m2.find()){
                String val = m2.group();
                cmd2 = "scoreboard objectives add "+val+" dummy "+val;
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd2);
                sender.sendMessage(ChatColor.GOLD+"写入一条记分板");
            }
            else if (zz1.find()){
                String val = null;
                String unSlove = zz1.group();
                Matcher zz2 = z2.matcher(unSlove);
                if (zz2.find()){
                    val = zz2.group();
                    cmd3 = "scoreboard objectives add "+val+" dummy "+val;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd3);
                    sender.sendMessage(ChatColor.GOLD+"写入一条记分板");
                }

            }
            else if (m3.find()){
                String val = m3.group();
                cmd4 = "scoreboard objectives add "+val+" dummy "+val;
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd4);
                sender.sendMessage(ChatColor.GOLD+"写入一条记分板");
            }
        }
    }




        int checkBlock(int x,int y,int z){
            Block b = world.getBlockAt(x,y,z);
            if (b.getType()==Material.COMMAND || b.getType()==Material.COMMAND_CHAIN || b.getType()==Material.COMMAND_REPEATING){
                return 1;
            }
            if (b.getType()==Material.COMMAND_MINECART){
                return 2;
            }
            return 0;

        }

    public WorldEditPlugin getwe(){
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin){
            return (WorldEditPlugin)p;
        }
        else {return null;}
    }




    public static String getCommandBlockCommand(Block block) {
        if (block.getType() == Material.COMMAND || block.getType() == Material.COMMAND_REPEATING
                ||block.getType() == Material.COMMAND_CHAIN) {
            return ((CommandBlock) block.getState()).getCommand();
        }
        return null;
    }

    public static String getMinecartCommandBlockCommand(Entity commandMinecart) {
        if (commandMinecart.getType() == EntityType.MINECART_COMMAND) {
            return ((CommandMinecart) commandMinecart).getCommand();
        }
        return null;
    }

}
