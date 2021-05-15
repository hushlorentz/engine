package greenpixel.game;

import greenpixel.game.data.Quest;
import greenpixel.game.data.QuestObjective;

import greenpixel.gut.data.BarrierData;
import greenpixel.gut.data.BreakableEntityData;
import greenpixel.gut.data.ButtonData;
import greenpixel.gut.data.EntityAnimationData;
import greenpixel.gut.data.EntityData;
import greenpixel.gut.data.ContainerData;
import greenpixel.gut.data.ConversationData;
import greenpixel.gut.data.DialogData;
import greenpixel.gut.data.DoorData;
import greenpixel.gut.data.InventoryData;
import greenpixel.gut.data.ItemData;
import greenpixel.gut.data.LootData;
import greenpixel.gut.data.LootDropData;
import greenpixel.gut.data.TriggerData;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;

public class DataManager
{
	public static DataManager instance;
	public ArrayList<ItemData> itemDataArray;
	public ArrayList<DoorData> doorDataArray;
	public ArrayList<BarrierData> barrierDataArray;
	public ArrayList<EntityData> characterDataArray;
	public ArrayList<ConversationData> conversationDataArray;
	public ArrayList<LootData> lootDataArray;
	public ArrayList<TriggerData> triggerDataArray;
	public ArrayList<Quest> questArray;
	public ArrayList<ButtonData> buttonArray;
	public String[] GameText;

	private ArrayList<BreakableEntityData> breakableDataArray;
	private ArrayList<ContainerData> containerDataArray;

	public DataManager()
	{
		itemDataArray = new ArrayList<ItemData>();
		doorDataArray = new ArrayList<DoorData>();
		barrierDataArray = new ArrayList<BarrierData>();
		characterDataArray = new ArrayList<EntityData>();
		containerDataArray = new ArrayList<ContainerData>();
		conversationDataArray = new ArrayList<ConversationData>();
		lootDataArray = new ArrayList<LootData>();
		breakableDataArray = new ArrayList<BreakableEntityData>();
		triggerDataArray = new ArrayList<TriggerData>();
		questArray = new ArrayList<Quest>();
		buttonArray = new ArrayList<ButtonData>();
	}

	public void loadGUTData(String path)
	{
		itemDataArray.clear();
		doorDataArray.clear();
		barrierDataArray.clear();
		characterDataArray.clear(); 
		containerDataArray.clear();
		conversationDataArray.clear();
		lootDataArray.clear();
		breakableDataArray.clear();
		triggerDataArray.clear();
		questArray.clear();
		buttonArray.clear();

		try
		{
			DataInputStream in = new DataInputStream(getClass().getClassLoader().getResourceAsStream(path));

			System.out.println("Loading GUT data: " + path);

			int numItems = in.readInt();

			for (int i = 0; i < numItems; i++)
			{
				ItemData iData = new ItemData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					iData.name += in.readChar();
				}

				int descriptionLength = in.readInt();

				for (int j = 0; j < descriptionLength; j++)
				{
					iData.description += in.readChar();
				}

				itemDataArray.add(iData);
			}

			int numDoors = in.readInt();

			for (int i = 0; i < numDoors; i++)
			{
				DoorData dData = new DoorData();

				dData.mapIndex = in.readInt();
				dData.xTile = in.readInt();
				dData.yTile = in.readInt();
				dData.closedAnimGroup = in.readInt();
				dData.closedAnimIndex = in.readInt();
				dData.openingAnimGroup = in.readInt();
				dData.openingAnimIndex = in.readInt();
				dData.openAnimGroup = in.readInt();
				dData.openAnimIndex = in.readInt();
				dData.lockType = in.readInt();
				dData.lockGroup = in.readInt();
				dData.lockIndex = in.readInt();
				dData.lockMessageIndex = in.readInt();
				dData.exitType = in.readInt();
				dData.exitTarget = in.readInt();
				dData.isVisible = in.readBoolean();
				dData.collisionBox.x = in.readInt();
				dData.collisionBox.y = in.readInt();
				dData.collisionBox.width = in.readInt();
				dData.collisionBox.height = in.readInt();
				dData.doorBox.x = in.readInt();
				dData.doorBox.y = in.readInt();
				dData.doorBox.width = in.readInt();
				dData.doorBox.height = in.readInt();
				doorDataArray.add(dData);
			}

			int numBarriers = in.readInt();

			for (int i = 0; i < numBarriers; i++)
			{
				BarrierData bData = new BarrierData();

				bData.closedAnimGroup = in.readInt();
				bData.closedAnimIndex = in.readInt();
				bData.openingAnimGroup = in.readInt();
				bData.openingAnimIndex = in.readInt();
				bData.openAnimGroup = in.readInt();
				bData.openAnimIndex = in.readInt();
				bData.lockType = in.readInt();
				bData.lockGroup = in.readInt();
				bData.lockIndex = in.readInt();
				bData.lockMessageIndex = in.readInt();
				bData.exitType = in.readInt();
				bData.exitTarget = in.readInt();
				bData.isAutomatic = in.readBoolean();
				bData.collisionBox.x = in.readInt();
				bData.collisionBox.y = in.readInt();
				bData.collisionBox.width = in.readInt();
				bData.collisionBox.height = in.readInt();
				barrierDataArray.add(bData);
			}

			int numCharacters = in.readInt();

			for (int i = 0; i < numCharacters; i++)
			{
				EntityData cData = new EntityData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					cData.displayName += in.readChar();
				}

				cData.aiType = in.readInt();
				cData.initialDirection = in.readInt();
				cData.collisionBox.x = in.readInt();
				cData.collisionBox.y = in.readInt();
				cData.collisionBox.width = in.readInt();
				cData.collisionBox.height = in.readInt();
				cData.hitBox.x = in.readInt();
				cData.hitBox.y = in.readInt();
				cData.hitBox.width = in.readInt();
				cData.hitBox.height = in.readInt();
				cData.health = in.readInt();
				cData.money = in.readInt();
				cData.experience = in.readInt();
				cData.attackCooldown = in.readInt();
				cData.defense = in.readFloat();
				cData.attack = in.readFloat();
				cData.speed = in.readFloat();
				cData.range = in.readFloat();
				cData.isMoveable = in.readBoolean();
				cData.isCollideable = in.readBoolean();
				cData.isFriendly = in.readBoolean();
				cData.isHittable = in.readBoolean();
				cData.zPos = in.readInt();

				int numAnimations = in.readInt();

				for (int j = 0; j < numAnimations; j++)
				{
					EntityAnimationData aData = new EntityAnimationData();

					int animNameLength = in.readInt();

					for (int k = 0; k < animNameLength; k++)
					{
						aData.name += in.readChar();
					}

					aData.upAnimGroup = in.readInt();
					aData.upAnimIndex = in.readInt();
					aData.downAnimGroup = in.readInt();
					aData.downAnimIndex = in.readInt();
					aData.leftAnimGroup = in.readInt();
					aData.leftAnimIndex = in.readInt();
					aData.rightAnimGroup = in.readInt();
					aData.rightAnimIndex = in.readInt();

					cData.animations.add(aData);
				}

				int dialogSize = in.readInt();

				for (int j = 0; j < dialogSize; j++)
				{
					cData.dialog.add(in.readInt());
				}

				int lootSize = in.readInt();

				for (int j = 0; j < lootSize; j++)
				{
					LootDropData lData = new LootDropData();

					lData.lootType = in.readInt();
					lData.minAmount = in.readInt();
					lData.maxAmount = in.readInt();
					lData.dropPercentage = in.readFloat();

					cData.loot.add(lData);
				}

				int questSize = in.readInt();

				for (int j = 0; j < questSize; j++)
				{
					Quest q = new Quest();

//					cData.quests.add(q);
				}

				characterDataArray.add(cData);
			}

			int numContainers = in.readInt();

			for (int i = 0; i < numContainers; i++)
			{
				ContainerData con = new ContainerData();
				con.lockType = in.readInt();
				con.lockGroup = in.readInt();
				con.lockIndex = in.readInt();
				con.lockMessageIndex = in.readInt();
				con.openMessageIndex = in.readInt();
				con.closedAnimGroup = in.readInt();
				con.closedAnimIndex = in.readInt();
				con.openingAnimGroup = in.readInt();
				con.openingAnimIndex = in.readInt();
				con.openAnimGroup = in.readInt();
				con.openAnimIndex = in.readInt();
				con.exitType = in.readInt();
				con.exitTarget = in.readInt();
				con.collisionBox.x = in.readInt();
				con.collisionBox.y = in.readInt();
				con.collisionBox.width = in.readInt();
				con.collisionBox.height = in.readInt();

				int numInventory = in.readInt();

				for (int j = 0; j < numInventory; j++)
				{
					InventoryData iData = new InventoryData();

					iData.itemType = in.readInt();
					iData.amount = in.readInt();

					con.inventory.add(iData);
				}

				containerDataArray.add(con);
			}

			int numLoot = in.readInt();

			for (int i = 0; i < numLoot; i++)
			{
				LootData lData = new LootData();

				lData.lootAnimGroup = in.readInt();
				lData.lootAnimIndex = in.readInt();
				lData.collisionBox.x = in.readInt();
				lData.collisionBox.y = in.readInt();
				lData.collisionBox.width = in.readInt();
				lData.collisionBox.height = in.readInt();

				int numInventory = in.readInt();

				for (int j = 0; j < numInventory; j++)
				{
					InventoryData iData = new InventoryData();

					iData.itemType = in.readInt();
					iData.amount = in.readInt();

					lData.inventory.add(iData);
				}

				lootDataArray.add(lData);
			}

			int numBreakable = in.readInt();

			for (int i = 0; i < numBreakable; i++)
			{
				BreakableEntityData bData = new BreakableEntityData();

				bData.collisionBox.x = in.readInt();
				bData.collisionBox.y = in.readInt();
				bData.collisionBox.width = in.readInt();
				bData.collisionBox.height = in.readInt();
				bData.hitBox.x = in.readInt();
				bData.hitBox.y = in.readInt();
				bData.hitBox.width = in.readInt();
				bData.hitBox.height = in.readInt();
				bData.fixedAnimGroup = in.readInt();
				bData.fixedAnimIndex = in.readInt();
				bData.breakingAnimGroup = in.readInt();
				bData.breakingAnimIndex = in.readInt();
				bData.brokenAnimGroup = in.readInt();
				bData.brokenAnimIndex = in.readInt();
				bData.health = in.readInt();

				int lootNum = in.readInt();

				for (int j = 0; j < lootNum; j++)
				{
					LootDropData lData = new LootDropData();

					lData.lootType = in.readInt();
					lData.minAmount = in.readInt();
					lData.maxAmount = in.readInt();
					lData.dropPercentage = in.readFloat();

					bData.loot.add(lData);
				}

				breakableDataArray.add(bData);
			}

			int numConvos = in.readInt();

			for (int i = 0; i < numConvos; i++)
			{
				ConversationData convo = new ConversationData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					convo.name += in.readChar();
				}

				int numDialog = in.readInt();

				for (int j = 0; j < numDialog; j++)
				{
					DialogData dData = new DialogData();

					int stringLength = in.readInt();

					for (int k = 0; k < stringLength; k++)
					{
						dData.text += in.readChar();
					}

					dData.animationGroup = in.readInt();
					dData.animationIndex = in.readInt();
					dData.textSpeed = in.readInt();
					dData.useAnimation = in.readBoolean();

					convo.dialog.add(dData);
				}
				
				conversationDataArray.add(convo);
			}

			int numTriggers = in.readInt();

			for (int i = 0; i < numTriggers; i++)
			{
				TriggerData tData = new TriggerData();

				tData.triggerType = in.readInt();
				tData.triggerGroup = in.readInt();
				tData.triggerIndex = in.readInt();
				tData.lockType = in.readInt();
				tData.lockGroup = in.readInt();
				tData.lockIndex = in.readInt();
				tData.collisionBox.x = in.readInt();
				tData.collisionBox.y = in.readInt();
				tData.collisionBox.width = in.readInt();
				tData.collisionBox.height = in.readInt();

				triggerDataArray.add(tData);
			}

			//load quests

			int numQuests = in.readInt();

			for (int i = 0; i < numQuests; i++)
			{
				Quest q = new Quest();

				int strLength = in.readInt();
				
				for (int j = 0; j < strLength; j++)
				{
					q.name += in.readChar();
				}

				strLength = in.readInt();

				for (int j = 0; j < strLength; j++)
				{
					q.description += in.readChar();
				}

				int numObjectives = in.readInt();

				for (int j = 0; j < numObjectives; j++)
				{
					QuestObjective qo = new QuestObjective();

					strLength = in.readInt();

					for (int k = 0; k < strLength; k++)
					{
						qo.name += in.readChar();
					}

					strLength = in.readInt();

					for (int k = 0; k < strLength; k++)
					{
						qo.description += in.readChar();
					}

					q.addObjective(qo);
				}

				questArray.add(q);
			}

			int buttonNum = in.readInt();

			for (int i = 0; i < buttonNum; i++)
			{
				ButtonData bData = new ButtonData();

				int nameLength = in.readInt();

				for (int j = 0; j < nameLength; j++)
				{
					bData.name += in.readChar();
				}

				int textLength = in.readInt();

				for (int j = 0; j < textLength; j++)
				{
					bData.text += in.readChar();
				}

				bData.upAnimGroup = in.readInt();
				bData.upAnimIndex = in.readInt();
				bData.downAnimGroup = in.readInt();
				bData.downAnimIndex = in.readInt();
				bData.selectedAnimGroup = in.readInt();
				bData.selectedAnimIndex = in.readInt();
				bData.collisionBox.x = in.readInt();
				bData.collisionBox.y = in.readInt();
				bData.collisionBox.width = in.readInt();
				bData.collisionBox.height = in.readInt();
				bData.textBox.x = in.readInt();
				bData.textBox.y = in.readInt();
				bData.textBox.width = in.readInt();
				bData.textBox.height = in.readInt();

				buttonArray.add(bData);
			}

			System.out.println("Loaded\t" + itemDataArray.size() + " items.");
			System.out.println("\t" + doorDataArray.size() + " doors.");
			System.out.println("\t" + barrierDataArray.size() + " barriers.");
			System.out.println("\t" + characterDataArray.size() + " characters.");
			for (int i = 0; i < characterDataArray.size(); i++)
			{
				EntityData cData = characterDataArray.get(i);

				System.out.println("\t\t -name: " + cData.displayName);
			}
			System.out.println("\t" + containerDataArray.size() + " containers.");
			System.out.println("\t" + conversationDataArray.size() + " conversations.");
			System.out.println("\t" + lootDataArray.size() + " loot.");
			System.out.println("\t" + breakableDataArray.size() + " breakable entities.");

			for (int i = 0; i < breakableDataArray.size(); i++)
			{
				System.out.println("\t\t -loot size: " + breakableDataArray.get(i).loot.size());
			}

			System.out.println("\t" + triggerDataArray.size() + " triggers.");
			System.out.println("\t" + questArray.size() + " quests");
			
			for (int i = 0; i < questArray.size(); i++)
			{
				System.out.println("\t\t" + questArray.get(i).name);
			}
			
			System.out.println("\t" + buttonArray.size() + " buttons.");

			in.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	public ContainerData getContainer(int index)
	{
		return containerDataArray.get(index);
	}

	public DoorData getDoor(int index)
	{
		return doorDataArray.get(index);
	}

	public BarrierData getBarrier(int index)
	{
		return barrierDataArray.get(index);
	}

	public ItemData getItem(int index)
	{
		return itemDataArray.get(index);
	}

	public ConversationData getConversation(int index)
	{
		return conversationDataArray.get(index);
	}

	public EntityData getCharacter(int index)
	{
		return characterDataArray.get(index);
	}

	public LootData getLoot(int index)
	{
		return lootDataArray.get(index);
	}

	public BreakableEntityData getBreakableEntity(int index)
	{
		return breakableDataArray.get(index);
	}

	public TriggerData getTrigger(int index)
	{
		return triggerDataArray.get(index);
	}

	public boolean hasCompletedQuest(int index)
	{
		return questArray.get(index).isComplete();
	}

	public boolean hasCompletedQuestObjective(int questIndex, int objectiveIndex)
	{
		return questArray.get(questIndex).isObjectiveComplete(objectiveIndex);
	}

	public void completeQuestObjective(int questIndex, int objectiveIndex)
	{
		questArray.get(questIndex).complete(objectiveIndex);
	}
}
