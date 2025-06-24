import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.item.ItemStack;
import org.amemeida.santiago.registry.blocks.ModBlocks;
import org.amemeida.santiago.registry.items.ModItems;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GeneralTests {

    /**
     * Configura o ambiente necessário antes da execução dos testes.
     */
    @BeforeAll
    static void beforeAll(){
        SharedConstants.createGameVersion();
        Bootstrap.initialize();
    }

    /**
     * Testa a criação de um ItemStack com uma quantidade específica do item.
     */
    @Test
    void TestItemCreation(){
        ItemStack item = new ItemStack(ModItems.SANTIAGUITA_INGOT, 3);

        Assertions.assertNotNull(item);
        Assertions.assertTrue(item.isOf(ModItems.SANTIAGUITA_INGOT));
        Assertions.assertEquals(3, item.getCount());
    }

    /**
     * Testa as propriedades específicas de um ItemStack, verificando componentes de dados.
     */
    @Test
    void TestItemProperties(){
        ItemStack item = new ItemStack(ModItems.STRIKE_TOTEM, 1);
        Assertions.assertEquals(item.getItem().getComponents().get(DataComponentTypes.DEATH_PROTECTION), DeathProtectionComponent.TOTEM_OF_UNDYING);
    }

    /**
     * Testa a criação de um ItemStack a partir de um bloco e a contagem correta.
     */
    @Test
    void TestBlockCreation(){
        ItemStack item = new ItemStack(ModBlocks.SANTIAGUITA_BLOCK, 2);
        Assertions.assertTrue(item.isOf(ModBlocks.SANTIAGUITA_BLOCK.asItem()));
        Assertions.assertEquals(2, item.getCount());
    }
}

