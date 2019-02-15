package com.revature.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.dao.PokemonDaoImpl;
import com.revature.dao.UserDaoImpl;
import com.revature.model.Pokedex;
import com.revature.model.Pokemon;
import com.revature.model.PokemonType;
import com.revature.model.User;

public class AppServices {
	
	final static Logger log = Logger.getLogger(UserDaoImpl.class);
	
	private static AppServices appService= null;
	
	private AppServices() {}
	
	public static AppServices getAppService() {
		if(appService == null) {
			appService = new AppServices();
		}
		return appService;
	}
	
	//TODO: test
	public Pokemon getPokemonById(int pokemonId) {
		PokemonDaoImpl pokemonDao = PokemonDaoImpl.getPokemonDao();
		return pokemonDao.getPokemonById(pokemonId);
	}
	
	//TODO: test
	public List<Pokemon> getAllPokemon(){
		PokemonDaoImpl pokemonDao = PokemonDaoImpl.getPokemonDao();
		return pokemonDao.getAllPokemon();
	}
	
	//TODO: test
	public int generateSaleValue(int pokemonId){
		PokemonDaoImpl pokemonDao = PokemonDaoImpl.getPokemonDao();
		int baseRarity = pokemonDao.getRarityByPokemonId(pokemonId);
		
		//CALCULATING SALE VALUE
		int saleValue = baseRarity/10;
		
		return saleValue;
	}
	
	//TODO: test
	public List<Pokemon> getPokemonByType(PokemonType pType){
		PokemonDaoImpl pokemonDao = PokemonDaoImpl.getPokemonDao();
		return pokemonDao.getPokemonByType(pType);
	}
	
	//TODO: test
	public List<String> getTypesByPokemonId(int pokemonId){
		PokemonDaoImpl pokemonDao = PokemonDaoImpl.getPokemonDao();
		return pokemonDao.getTypesByPokemonId(pokemonId);
	}
	
	//TODO: test
	public int getRarityByPokemonId(int pokemonId){
		PokemonDaoImpl pokemonDao = PokemonDaoImpl.getPokemonDao();
		return pokemonDao.getRarityByPokemonId(pokemonId);
	}

	//TODO: test
	public int sellAllDuplicatePokemonByUserId(int userId){
		UserDaoImpl userDao = UserDaoImpl.getUserDao();
		int newCreditTotal = userDao.getUserCredit(userId);
		PokemonDaoImpl pokemonDao = PokemonDaoImpl.getPokemonDao();
		List<Pokemon> userPokemon = pokemonDao.getPokemonByUserId(userId);
		Iterator<Pokemon> it = userPokemon.iterator();
		while(it.hasNext()) {
			Pokemon nextPokemon = (Pokemon)it.next();
			int pokemonId = nextPokemon.getPokemonId();
			while(nextPokemon.getCount() > 1) {
				if(pokemonDao.decrementPokemonCountByUserAndPokemonId(userId, pokemonId)) {
					newCreditTotal = generateSaleValue(pokemonId);
				}
			}
		}
		
		userDao.updateUserCredit(newCreditTotal);
		return newCreditTotal;
	}
	
	//TODO: test
	public int sellDuplicateByUserAndPokemonId(int uId, int pId){
		UserDaoImpl userDao = UserDaoImpl.getUserDao();
		int newCreditTotal = userDao.getUserCredit(uId);
		PokemonDaoImpl pokemonDao = PokemonDaoImpl.getPokemonDao();
		int count = pokemonDao.getPokemonCountByUserIdAndPokemonId(uId, pId);
		if(count > 0) {
			pokemonDao.decrementPokemonCountByUserAndPokemonId(uId, pId);
			newCreditTotal += generateSaleValue(pId);
		}
		
		userDao.updateUserCredit(newCreditTotal);
		return newCreditTotal;
	}
	
	public Pokemon generateAndAddRandomPokemon(int uId){
		return null;
	}
	
	//TODO: test
	public Pokemon buyPokemon(int uId, int pId){
		PokemonDaoImpl pokemonDao = PokemonDaoImpl.getPokemonDao();
		UserDaoImpl userDao = UserDaoImpl.getUserDao();
		
		Pokemon pokemonToBuy = null;
		
		int pokemonCost = generateSaleValue(pId);
		int userCredits = userDao.getUserCredit(uId);
		
		if(userCredits > pokemonCost) {
			userCredits -= pokemonCost;
			userDao.updateUserCredit(userCredits);
			pokemonDao.addPokemonByUserIdAndPokemonId(uId, pId);
			pokemonToBuy = pokemonDao.getPokemonById(pId);
		}
		
		return pokemonToBuy;
	}
	
	public List<User> getLeaderBoard (){
		UserDaoImpl userDao = UserDaoImpl.getUserDao();
		return userDao.getLeaderBoard();
	}
	
	public User checkUserCredentials(String username, String password){
		
		UserDaoImpl userDao = UserDaoImpl.getUserDao();
		return userDao.checkUserCredentials(username, password);
	}
	
	public boolean createUser(User newUser){
		UserDaoImpl userDao = UserDaoImpl.getUserDao();
		return userDao.createUser(newUser);
	}
	
	//TODO: test
	public User getUserById(int uId){
		UserDaoImpl userDao = UserDaoImpl.getUserDao();
		return userDao.getUserById(uId);
	}
	
	public boolean validateUsername(String username) {
		UserDaoImpl userDao = UserDaoImpl.getUserDao();
		return userDao.validateUsername(username);
	}
	
	public boolean validateEmail(String email) {
		UserDaoImpl userDao = UserDaoImpl.getUserDao();
		return userDao.validateUsername(email);
	}
}

