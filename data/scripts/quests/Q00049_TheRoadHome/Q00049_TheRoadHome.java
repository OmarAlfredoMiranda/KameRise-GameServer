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
package quests.Q00049_TheRoadHome;

import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00049_TheRoadHome extends Quest
{
	// NPCs
	private static final int GALLADUCCI = 30097;
	private static final int GENTLER = 30094;
	private static final int SANDRA = 30090;
	private static final int DUSTIN = 30116;
	// Items
	private static final int ORDER_DOCUMENT_1 = 7563;
	private static final int ORDER_DOCUMENT_2 = 7564;
	private static final int ORDER_DOCUMENT_3 = 7565;
	private static final int MAGIC_SWORD_HILT = 7568;
	private static final int GEMSTONE_POWDER = 7567;
	private static final int PURIFIED_MAGIC_NECKLACE = 7566;
	private static final int MARK_OF_TRAVELER = 7570;
	private static final int SCROLL_OF_ESCAPE_SPECIAL = 7558;
	
	public Q00049_TheRoadHome()
	{
		super(49);
		registerQuestItems(ORDER_DOCUMENT_1, ORDER_DOCUMENT_2, ORDER_DOCUMENT_3, MAGIC_SWORD_HILT, GEMSTONE_POWDER, PURIFIED_MAGIC_NECKLACE);
		addStartNpc(GALLADUCCI);
		addTalkId(GALLADUCCI, GENTLER, SANDRA, DUSTIN);
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
		
		switch (event)
		{
			case "30097-03.htm":
			{
				st.startQuest();
				giveItems(player, ORDER_DOCUMENT_1, 1);
				break;
			}
			case "30094-02.htm":
			{
				st.setCond(2, true);
				takeItems(player, ORDER_DOCUMENT_1, 1);
				giveItems(player, MAGIC_SWORD_HILT, 1);
				break;
			}
			case "30097-06.htm":
			{
				st.setCond(3, true);
				takeItems(player, MAGIC_SWORD_HILT, 1);
				giveItems(player, ORDER_DOCUMENT_2, 1);
				break;
			}
			case "30090-02.htm":
			{
				st.setCond(4, true);
				takeItems(player, ORDER_DOCUMENT_2, 1);
				giveItems(player, GEMSTONE_POWDER, 1);
				break;
			}
			case "30097-09.htm":
			{
				st.setCond(5, true);
				takeItems(player, GEMSTONE_POWDER, 1);
				giveItems(player, ORDER_DOCUMENT_3, 1);
				break;
			}
			case "30116-02.htm":
			{
				st.setCond(6, true);
				takeItems(player, ORDER_DOCUMENT_3, 1);
				giveItems(player, PURIFIED_MAGIC_NECKLACE, 1);
				break;
			}
			case "30097-12.htm":
			{
				takeItems(player, MARK_OF_TRAVELER, -1);
				takeItems(player, PURIFIED_MAGIC_NECKLACE, 1);
				rewardItems(player, SCROLL_OF_ESCAPE_SPECIAL, 1);
				st.exitQuest(false, true);
				break;
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
				if ((player.getRace() == Race.DWARF) && (player.getLevel() >= 3))
				{
					if (hasQuestItems(player, MARK_OF_TRAVELER))
					{
						htmltext = "30097-02.htm";
					}
					else
					{
						htmltext = "30097-01.htm";
					}
				}
				else
				{
					htmltext = "30097-01a.htm";
				}
				break;
			}
			case State.STARTED:
			{
				final int cond = st.getCond();
				switch (npc.getId())
				{
					case GALLADUCCI:
					{
						if (cond == 1)
						{
							htmltext = "30097-04.htm";
						}
						else if (cond == 2)
						{
							htmltext = "30097-05.htm";
						}
						else if (cond == 3)
						{
							htmltext = "30097-07.htm";
						}
						else if (cond == 4)
						{
							htmltext = "30097-08.htm";
						}
						else if (cond == 5)
						{
							htmltext = "30097-10.htm";
						}
						else if (cond == 6)
						{
							htmltext = "30097-11.htm";
						}
						break;
					}
					case GENTLER:
					{
						if (cond == 1)
						{
							htmltext = "30094-01.htm";
						}
						else if (cond > 1)
						{
							htmltext = "30094-03.htm";
						}
						break;
					}
					case SANDRA:
					{
						if (cond == 3)
						{
							htmltext = "30090-01.htm";
						}
						else if (cond > 3)
						{
							htmltext = "30090-03.htm";
						}
						break;
					}
					case DUSTIN:
					{
						if (cond == 5)
						{
							htmltext = "30116-01.htm";
						}
						else if (cond == 6)
						{
							htmltext = "30116-03.htm";
						}
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
