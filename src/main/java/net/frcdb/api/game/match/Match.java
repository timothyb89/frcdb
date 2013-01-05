package net.frcdb.api.game.match;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.frcdb.api.game.event.Game;
import net.frcdb.api.team.Team;
import net.frcdb.content.Content;
import net.frcdb.content.ContentProvider;
import net.frcdb.util.ListUtil;

/**
 *
 * @author tim
 */
@Cache
@Entity
public class Match implements ContentProvider {
	
	public static final int SCORE_UNKNOWN = -1;
	public static final int SCORE_DISQUALIFIED = -10000;

	@Parent
	private Ref<Game> game;
	
	@Index private MatchType type;
	private String time;
	@Index private int number;
	
	@Load @Index private List<Ref<Team>> redTeams;
	@Load @Index private List<Ref<Team>> blueTeams;

	private int blueScore;
	private int redScore;
	
	private List<Content> content;
	
	@Id
	private Long id;

	public Match() {
		redTeams = new ArrayList<Ref<Team>>();
		blueTeams = new ArrayList<Ref<Team>>();
		
		content = new ArrayList<Content>();
	}
	
	public Match(Game game) {
		this.game = Ref.create(game);
		
		redTeams = new ArrayList<Ref<Team>>();
		blueTeams = new ArrayList<Ref<Team>>();
		
		content = new ArrayList<Content>();
	}

	public MatchType getType() {
		return type;
	}

	public void setType(MatchType type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void addRedTeam(Team t) {
		redTeams.add(Ref.create(t));
	}

	public void addBlueTeam(Team t) {
		blueTeams.add(Ref.create(t));
	}

	/**
	 * Returns a list of all teams on the blue alliance for this match. Note
	 * that as all references are automatically resolved the returned list will
	 * be unmodifiable. To modify the contents of this list, use
	 * {@link addBlueTeam(Team)}.
	 * @return an unmodifiable list of blue teams in this match
	 */
	public List<Team> getBlueTeams() {
		return Collections.unmodifiableList(ListUtil.resolveRefs(blueTeams));
	}

	/**
	 * Returns a list of all teams on the red alliance for this match. Note
	 * that as all references are automatically resolved the returned list will
	 * be unmodifiable. To modify the contents of this list, use
	 * {@link addRedTeam(Team)}.
	 * @return an unmodifiable list of red teams in this match
	 */
	public List<Team> getRedTeams() {
		return Collections.unmodifiableList(ListUtil.resolveRefs(redTeams));
	}

	public Alliance getAlliance(Team team) {
		Ref<Team> ref = Ref.create(team);
		
		if (redTeams.contains(ref)) {
			return Alliance.RED;
		} else if (blueTeams.contains(ref)) {
			return Alliance.BLUE;
		} else {
			return null;
		}
	}

	public Alliance getWinningAlliance() {
		if (redScore > blueScore) {
			return Alliance.RED;
		} else if (blueScore > redScore) {
			return Alliance.BLUE;
		} else {
			return Alliance.TIE;
		}
	}

	/**
	 * Returns a list of all teams that participated in any of the alliances in
	 * this match. Note that this list is immutable (via
	 * {@link Collections#unmodifiableList(List)} as it automatically resolves
	 * database references. To modify the contents of the list, use
	 * {@link addRedTeam(Team)} and {@link addBlueTeam(Team)}.
	 * @return an immutable list of teams in this match
	 */
	public List<Team> getTeams() {
		List<Team> ret = new ArrayList<Team>(
				redTeams.size() + blueTeams.size());
		
		ret.addAll(ListUtil.resolveRefs(blueTeams));
		ret.addAll(ListUtil.resolveRefs(redTeams));

		return Collections.unmodifiableList(ret);
	}

	public int getBlueScore() {
		return blueScore;
	}

	public void setBlueScore(int blueScore) {
		this.blueScore = blueScore;
	}

	public int getRedScore() {
		return redScore;
	}

	public void setRedScore(int redScore) {
		this.redScore = redScore;
	}

	public String getIdentifier() {
		return type.getPrefix().toLowerCase() + number;
	}

	@Override
	public Object getRecordId() {
		return id;
	}

	@Override
	public String getContentType() {
		return "Match";
	}
	
	@Override
	public List<Content> getContent() {
		return content;
	}
	
	@Override
	public void addContent(Content c) {
		content.add(c);
	}
	
	@Override
	public void removeContent(Content c) {
		content.remove(c);
	}
	
	@Override
	public String toString() {
		String red = "[" +
				redTeams.get(0) + "," +
				redTeams.get(1) + "," +
				redTeams.get(2) + "]";
		String blue = "[" +
				blueTeams.get(0) + "," +
				blueTeams.get(1) + "," +
				blueTeams.get(2) + "]";

		return "Match[type=" + type + ", number=" + number + ", red=" + red
				+ ", blue=" + blue + "]";
	}

}
