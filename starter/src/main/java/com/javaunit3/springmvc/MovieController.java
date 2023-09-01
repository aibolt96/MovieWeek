package com.javaunit3.springmvc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private BestMovieService bestMovieService;

    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping("/")
    public String getIndexPage(){
        return "index";
    }

    @RequestMapping("/addMovieForm")
    public String addMovieForm(){
        return "addMovie";
    }

    @RequestMapping("/bestMovie")
    public String getBestMoviePage(Model model){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<MovieEntity> movieEntityList = session.createQuery("from MovieEntity").list();
        movieEntityList.sort(Comparator.comparingInt(movieEntity -> movieEntity.getVotes().size()));
        MovieEntity movieWithMostVotes = movieEntityList.get(movieEntityList.size() - 1); List<String>
                voterNames = new ArrayList<>();
                for(VoteEntity vote: movieWithMostVotes.getVotes()){
                    voterNames.add(vote.getVoterName());
                }
                String voterNameList = String.join(",", voterNames);
                model.addAttribute("bestMovie", movieWithMostVotes.getTitle());
                model.addAttribute("bestMovieVoters", voterNameList);
                session.getTransaction().commit();
                return "bestMovie";
    }

    @RequestMapping("/voteForBestMovieForm")
    public String voteForBestMovieFormPage(Model model){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<MovieEntity> movieEntityList = session.createQuery("from MovieEntity").list();
        session.getTransaction().commit();
        model.addAttribute("movies", movieEntityList);
        return "voteForBestMovie";
    }

    @RequestMapping("/voteForBestMovie")
    public String voteForBestMovie(HttpServletRequest request, Model model){
        String movieTitle = request.getParameter("movieTitle");
        String movieId = request.getParameter("movieId");
        String votersName = request.getParameter("votersName");

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        MovieEntity movieEntity = (MovieEntity) session.get(MovieEntity.class, Integer.parseInt(movieId));
        VoteEntity newVote = new VoteEntity();
        newVote.setVotersName(votersName);
        movieEntity.addVote(newVote);
        model.addAttribute("BestMovieVote", movieTitle);

        session.update(movieEntity);
        session.getTransaction().commit();
        return "voteForBestMovie";
    }
}
