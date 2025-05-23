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
package quests.Q00152_ShardsOfGolem;

import org.l2jmobius.gameserver.enums.QuestSound;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00152_ShardsOfGolem extends Quest
{
	// NPCs
	private static final int HARRIS = 30035;
	private static final int ALTRAN = 30283;
	// Monster
	private static final int STONE_GOLEM = 20016;
	// Items
	private static final int HARRIS_RECEIPT_1 = 1008;
	private static final int HARRIS_RECEIPT_2 = 1009;
	private static final int GOLEM_SHARD = 1010;
	private static final int TOOL_BOX = 1011;
	// Reward
	private static final int WOODEN_BREASTPLATE = 23;
	
	public Q00152_ShardsOfGolem()
	{
		super(152);
		registerQuestItems(HARRIS_RECEIPT_1, HARRIS_RECEIPT_2, GOLEM_SHARD, TOOL_BOX);
		addStartNpc(HARRIS);
		addTalkId(HARRIS, ALTRAN);
		addKillId(STONE_GOLEM);
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
		
		if (event.equals("30035-02.htm"))
		{
			st.startQuest();
			giveItems(player, HARRIS_RECEIPT_1, 1);
		}
		else if (event.equals("30283-02.htm"))
		{
			st.setCond(2, true);
			takeItems(player, HARRIS_RECEIPT_1, 1);
			giveItems(player, HARRIS_RECEIPT_2, 1);
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
				htmltext = (player.getLevel() < 10) ? "30035-01a.htm" : "30035-01.htm";
				break;
			}
			case State.STARTED:
			{
				final int cond = st.getCond();
				switch (npc.getId())
				{
					case HARRIS:
					{
						if (cond < 4)
						{
							htmltext = "30035-03.htm";
						}
						else if (cond == 4)
						{
							htmltext = "30035-04.htm";
							takeItems(player, HARRIS_RECEIPT_2, 1);
							takeItems(player, TOOL_BOX, 1);
							giveItems(player, WOODEN_BREASTPLATE, 1);
							addExpAndSp(player, 5000, 0);
							st.exitQuest(false, true);
						}
						break;
					}
					case ALTRAN:
					{
						if (cond == 1)
						{
							htmltext = "30283-01.htm";
						}
						else if (cond == 2)
						{
							htmltext = "30283-03.htm";
						}
						else if (cond == 3)
						{
							htmltext = "30283-04.htm";
							st.setCond(4, true);
							takeItems(player, GOLEM_SHARD, -1);
							giveItems(player, TOOL_BOX, 1);
						}
						else if (cond == 4)
						{
							htmltext = "30283-05.htm";
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
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		final QuestState qs = getQuestState(killer, false);
		if ((qs != null) && qs.isCond(2) && (getRandom(100) < 30) && (getQuestItemsCount(killer, GOLEM_SHARD) < 5))
		{
			giveItems(killer, GOLEM_SHARD, 1);
			if (getQuestItemsCount(killer, GOLEM_SHARD) >= 5)
			{
				qs.setCond(3, true);
			}
			else
			{
				playSound(killer, QuestSound.ITEMSOUND_QUEST_ITEMGET);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
}
