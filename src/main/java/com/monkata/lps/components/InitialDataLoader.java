/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monkata.lps.components;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.monkata.lps.Game.Game;
import com.monkata.lps.Game.GameMaster;
import com.monkata.lps.Game.ModeGame;
import com.monkata.lps.Game.ModeGameMaster;
import com.monkata.lps.Game.ParamsGame;
import com.monkata.lps.Game.WinName;
import com.monkata.lps.dao.BankRepository;
import com.monkata.lps.dao.GameMasterRepository;
import com.monkata.lps.dao.GameRepository;
import com.monkata.lps.dao.ModeGameMasterRepository;
import com.monkata.lps.dao.ModeGameRepository;
import com.monkata.lps.dao.PVBankRepository;
import com.monkata.lps.dao.ParamsGameRepository;
import com.monkata.lps.dao.RoleRepository;
import com.monkata.lps.dao.UserRepository;
import com.monkata.lps.dao.WinNameRepository;
import com.monkata.lps.entity.Bank;
import com.monkata.lps.entity.PVBank;
import com.monkata.lps.entity.Role;
import com.monkata.lps.entity.UserEntity;


@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
 
    boolean alreadySetup = false;
     
    @Autowired
    public UserRepository userRepository;
  
    @Autowired
    private RoleRepository roleRepository;
     
    @Autowired
    private GameRepository game;
    
    @Autowired
    private GameMasterRepository sgame;
    
    @Autowired
    private ModeGameRepository mgame;
    
    @Autowired
    private ModeGameMasterRepository mgamem;
    
    @Autowired
    private ParamsGameRepository pgame;
    
    @Autowired
    private BankRepository bank;
    
    @Autowired
    private PVBankRepository pvbank;
    
    @Autowired
    private WinNameRepository winName;
    
  
    
    @Transactional
    private void setGame() {
    	
    	Bank bank = StaticData.bank;
        bank =  this.bank.save(bank);   
        
        PVBank pvbank = StaticData.pvbank;
        // pvbank.setId_bank(bank.getId());
        pvbank =  this.pvbank.save(pvbank); 
    	
    	for(WinName wn : StaticData.winNames ) {
    		winName.save(wn);
    	}
    	
    	ParamsGame pg = new ParamsGame();
        pg.setName("Defaut");
        pg.setIs_for_client(1);
        pg = pgame.save(pg);
        List<GameMaster> gmst = new ArrayList<>();
        List<Game> gs = new ArrayList<>();
        List<ModeGame> mgs = new ArrayList<>();
        for(GameMaster g: StaticData.gamemaster) {
          gmst.add(sgame.save(g));
        }
    	 for(GameMaster gms : gmst) {
    	      Game g = new Game();	
    	      g.setGamemaster(gms);
    		  g.setParamsgame(pg);
	          g = game.save(g);
    		  Game ng =  game.save(g);
	          gs.add(ng) ; 
    	}
    	Long id =(long) 3;
    	for(Game g : gs) {
		   for(ModeGame mg : StaticData.modeGames) {
			 mgamem.save(new ModeGameMaster(mg));
			 mg.setId(id);
			 mg.setGame(g); 
			 mg.setPoint_per_price(1);
       	     ModeGame nmg = mgame.save(mg);
       	     id++;
       	     g.setOneGame(nmg);
            }  
		   game.save(g);
    	}		  
    	addAdmin(bank.getId());
    	addUser(pg.getId(),bank.getId(), pvbank.getId());
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
    
        if (alreadySetup)
            return;
        createRoleIfNotFound(RoleName.MASTER);
        createRoleIfNotFound(RoleName.ADMIN);
        createRoleIfNotFound(RoleName.SELLER);
        createRoleIfNotFound(RoleName.CLIENT);
        createRoleIfNotFound(RoleName.SUPERVISOR);
        createRoleIfNotFound(RoleName.TECH);
        //*********************
        Long nu = userRepository.count();
        if(nu == 0L){ 	
          // setUp game 
        	setGame();
        }
        
        alreadySetup = true;
    }
    
    private void addAdmin(Long bid) {
    	Role adminRole = roleRepository.findByName(RoleName.ADMIN);
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setFirstName("Admin");
        user.setLastName("");
        String password = "lolo91";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        user.setPassword(encodedPassword);
        user.setUsername("admin");
        user.setRole(adminRole);
        user.setEnabled(true);
        user.setBank(bid);
        user.setLock(false);
        userRepository.save(user);
	}
    
    
    
    private void addUser(Long id,Long bid, Long pvid) {
    	Role adminRole = roleRepository.findByName(RoleName.SELLER);
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setFirstName("Seller");
        user.setLastName("");
        String password = "lolo91";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        user.setPassword(encodedPassword);
        user.setUsername("seller");
        user.setRole(adminRole);
        user.setEnabled(true);
        user.setParamgame(id);
        user.setLock(false);
        // id pv bnk 
        user.setPvbank(pvid);
        // id bank 
        user.setBank(bid);
        userRepository.save(user);
	}

	@Transactional
    private Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }
    
 
}