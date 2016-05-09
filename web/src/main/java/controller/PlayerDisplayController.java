package controller;

import java.util.List;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.stanislav.PlayerEJB;
import com.stanislav.model.Player;
import com.stanislav.model.PlayerStatistics;

public class PlayerDisplayController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		ModelAndView model = new ModelAndView("PlayerDisplayController");

		InitialContext context = new InitialContext();
		PlayerEJB playerEJB = (PlayerEJB) context.lookup("java:app/Hockey-ejb/PlayerEJB");
		Long id = Long.valueOf(req.getParameter("id"));
		Player player = playerEJB.getPlayerById(id);
		model.addObject("player", player);
		List<PlayerStatistics> playerStatistics = player.getPlayerStatistics();
		
		for(PlayerStatistics statistic : playerStatistics){
			if (statistic.getYear() == 2015 && !statistic.isPlayoffStatistics()){
				model.addObject("seasonStatistics", statistic);
			}
			if(statistic.getYear() == 2015 && statistic.isPlayoffStatistics()){
				model.addObject("playoffStatistics", statistic);
			} 
		}
		Object[] resultSeason = playerEJB.getSumOfPlayerStatistics(player, false);
		Object[] resultPlayoff = playerEJB.getSumOfPlayerStatistics(player, true);
		
		model.addObject("sumOfSeasons", resultSeason);
		model.addObject("sumOfPlayoffs", resultPlayoff);
		
		return model;
	}

}