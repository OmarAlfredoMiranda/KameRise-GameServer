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
package quests.Q00385_YokeOfThePast;

import java.util.HashMap;
import java.util.Map;

import org.l2jmobius.gameserver.enums.QuestSound;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;

public class Q00385_YokeOfThePast extends Quest
{
	// NPCs
	private static final int[] GATEKEEPER_ZIGGURAT =
	{
		31095,
		31096,
		31097,
		31098,
		31099,
		31100,
		31101,
		31102,
		31103,
		31104,
		31105,
		31106,
		31107,
		31108,
		31109,
		31110,
		31114,
		31115,
		31116,
		31117,
		31118,
		31119,
		31120,
		31121,
		31122,
		31123,
		31124,
		31125,
		31126
	};
	// Item
	private static final int ANCIENT_SCROLL = 5902;
	// Reward
	private static final int BLANK_SCROLL = 5965;
	// Drop chances
	private static final Map<Integer, Integer> CHANCES = new HashMap<>();
	static
	{
		CHANCES.put(21208, 70000);
		CHANCES.put(21209, 80000);
		CHANCES.put(21210, 110000);
		CHANCES.put(21211, 110000);
		CHANCES.put(21213, 140000);
		CHANCES.put(21214, 190000);
		CHANCES.put(21215, 190000);
		CHANCES.put(21217, 240000);
		CHANCES.put(21218, 300000);
		CHANCES.put(21219, 300000);
		CHANCES.put(21221, 370000);
		CHANCES.put(21222, 460000);
		CHANCES.put(21223, 450000);
		CHANCES.put(21224, 500000);
		CHANCES.put(21225, 540000);
		CHANCES.put(21226, 660000);
		CHANCES.put(21227, 640000);
		CHANCES.put(21228, 700000);
		CHANCES.put(21229, 750000);
		CHANCES.put(21230, 910000);
		CHANCES.put(21231, 860000);
		CHANCES.put(21236, 120000);
		CHANCES.put(21237, 140000);
		CHANCES.put(21238, 190000);
		CHANCES.put(21239, 190000);
		CHANCES.put(21240, 220000);
		CHANCES.put(21241, 240000);
		CHANCES.put(21242, 300000);
		CHANCES.put(21243, 300000);
		CHANCES.put(21244, 340000);
		CHANCES.put(21245, 370000);
		CHANCES.put(21246, 460000);
		CHANCES.put(21247, 450000);
		CHANCES.put(21248, 500000);
		CHANCES.put(21249, 540000);
		CHANCES.put(21250, 660000);
		CHANCES.put(21251, 640000);
		CHANCES.put(21252, 700000);
		CHANCES.put(21253, 750000);
		CHANCES.put(21254, 910000);
		CHANCES.put(21255, 860000);
	}
	
	public Q00385_YokeOfThePast()
	{
		super(385);
		registerQuestItems(ANCIENT_SCROLL);
		addStartNpc(GATEKEEPER_ZIGGURAT);
		addTalkId(GATEKEEPER_ZIGGURAT);
		addKillId(CHANCES.keySet());
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
		
		if (event.equals("05.htm"))
		{
			st.startQuest();
		}
		else if (event.equals("10.htm"))
		{
			st.exitQuest(true, true);
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
				htmltext = (player.getLevel() < 20) ? "02.htm" : "01.htm";
				break;
			}
			case State.STARTED:
			{
				if (!hasQuestItems(player, ANCIENT_SCROLL))
				{
					htmltext = "08.htm";
				}
				else
				{
					htmltext = "09.htm";
					final int count = getQuestItemsCount(player, ANCIENT_SCROLL);
					takeItems(player, ANCIENT_SCROLL, -1);
					rewardItems(player, BLANK_SCROLL, count);
				}
				break;
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, Player player, boolean isPet)
	{
		final QuestState st = getRandomPartyMemberState(player, -1, 3, npc);
		if (st == null)
		{
			return null;
		}
		final Player partyMember = st.getPlayer();
		
		if (getRandom(1000000) < CHANCES.get(npc.getId()))
		{
			giveItems(partyMember, ANCIENT_SCROLL, 1);
			playSound(partyMember, QuestSound.ITEMSOUND_QUEST_ITEMGET);
		}
		
		return null;
	}
}
