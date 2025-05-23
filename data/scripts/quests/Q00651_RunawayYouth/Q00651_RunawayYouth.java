/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests.Q00651_RunawayYouth;

import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.network.serverpackets.MagicSkillUse;

public class Q00651_RunawayYouth extends Quest
{
	// NPCs
	private static final int IVAN = 32014;
	private static final int BATIDAE = 31989;
	// Item
	private static final int SCROLL_OF_ESCAPE = 736;
	// Table of possible spawns
	private static final Location[] SPAWNS =
	{
		new Location(118600, -161235, -1119, 0),
		new Location(108380, -150268, -2376, 0),
		new Location(123254, -148126, -3425, 0)
	};
	// Current position
	private int _currentPosition = 0;
	
	public Q00651_RunawayYouth()
	{
		super(651);
		addStartNpc(IVAN);
		addTalkId(IVAN, BATIDAE);
		addSpawn(IVAN, 118600, -161235, -1119, 0, false, 0);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		final QuestState st = getQuestState(player, false);
		if (st == null)
		{
			return htmltext;
		}
		
		if (event.equals("32014-04.htm"))
		{
			if (hasQuestItems(player, SCROLL_OF_ESCAPE))
			{
				st.startQuest();
				takeItems(player, SCROLL_OF_ESCAPE, 1);
				npc.broadcastPacket(new MagicSkillUse(npc, npc, 2013, 1, 3500, 0));
				startQuestTimer("apparition_npc", 4000, npc, player, false);
				htmltext = "32014-03.htm";
			}
			else
			{
				st.exitQuest(true);
			}
		}
		else if (event.equals("apparition_npc"))
		{
			int chance = getRandom(3);
			
			// Loop to avoid to spawn to the same place.
			while (chance == _currentPosition)
			{
				chance = getRandom(3);
			}
			
			// Register new position.
			_currentPosition = chance;
			
			npc.deleteMe();
			addSpawn(IVAN, SPAWNS[chance], false, 0);
			return null;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		
		switch (st.getState())
		{
			case State.CREATED:
			{
				htmltext = (player.getLevel() < 26) ? "32014-01.htm" : "32014-02.htm";
				break;
			}
			case State.STARTED:
			{
				switch (npc.getId())
				{
					case BATIDAE:
					{
						htmltext = "31989-01.htm";
						giveAdena(player, 2883, true);
						st.exitQuest(true, true);
						break;
					}
					case IVAN:
					{
						htmltext = "32014-04a.htm";
						break;
					}
				}
				break;
			}
		}
		
		return htmltext;
	}
}
