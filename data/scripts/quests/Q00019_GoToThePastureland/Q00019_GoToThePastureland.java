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
package quests.Q00019_GoToThePastureland;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00019_GoToThePastureland extends Quest
{
	// NPCs
	private static final int VLADIMIR = 31302;
	private static final int TUNATUN = 31537;
	// Items
	private static final int YOUNG_WILD_BEAST_MEAT = 7547;
	
	public Q00019_GoToThePastureland()
	{
		super(19);
		registerQuestItems(YOUNG_WILD_BEAST_MEAT);
		addStartNpc(VLADIMIR);
		addTalkId(VLADIMIR, TUNATUN);
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
		
		if (event.equals("31302-01.htm"))
		{
			st.startQuest();
			giveItems(player, YOUNG_WILD_BEAST_MEAT, 1);
		}
		else if (event.equals("019_finish"))
		{
			if (hasQuestItems(player, YOUNG_WILD_BEAST_MEAT))
			{
				htmltext = "31537-01.htm";
				takeItems(player, YOUNG_WILD_BEAST_MEAT, 1);
				giveAdena(player, 30000, true);
				st.exitQuest(false, true);
			}
			else
			{
				htmltext = "31537-02.htm";
			}
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
				htmltext = (player.getLevel() < 63) ? "31302-03.htm" : "31302-00.htm";
				break;
			}
			case State.STARTED:
			{
				switch (npc.getId())
				{
					case VLADIMIR:
					{
						htmltext = "31302-02.htm";
						break;
					}
					case TUNATUN:
					{
						htmltext = "31537-00.htm";
						break;
					}
				}
				break;
			}
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		
		return htmltext;
	}
}
