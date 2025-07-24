package gecko10000.geckolib.misc

import gecko10000.geckolib.GeckoLib
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.function.Consumer

/**
 * Simple utility for Bukkit scheduler tasks, essentially just shorthand
 */
class Task private constructor(
    private val task: Int,
    /**
     * @return The type of this Task
     */
    val type: TaskType,
    /**
     * @return The Plugin which scheduled this task
     */
    val plugin: Plugin
) {
    val isQueued: Boolean
        /**
         * @return Whether this Task is queued, same as [org.bukkit.scheduler.BukkitScheduler.isQueued]
         */
        get() = Bukkit.getScheduler().isQueued(task)

    val isCurrentlyRunning: Boolean
        /**
         * @return Whether this Task is currently running, same as [org.bukkit.scheduler.BukkitScheduler.isCurrentlyRunning]
         */
        get() = Bukkit.getScheduler().isCurrentlyRunning(task)

    /**
     * Cancels this task, same as [org.bukkit.scheduler.BukkitScheduler.cancelTask]
     */
    fun cancel() {
        Bukkit.getScheduler().cancelTask(task)
    }

    /**
     * Represents a type of task
     */
    enum class TaskType {
        SYNC_DELAYED,
        ASYNC_DELAYED,
        SYNC_REPEATING,
        ASYNC_REPEATING
    }

    companion object {
        /**
         * Schedules a sync delayed task to run as soon as possible
         *
         * @param run The task to run
         * @return The Task that has been scheduled
         */
        fun syncDelayed(run: Runnable?): Task? {
            return syncDelayed(GeckoLib.get(), run!!)
        }

        /**
         * Schedules a sync delayed task to run as soon as possible
         *
         * @param run The task to run
         * @return The Task that has been scheduled
         */
        fun syncDelayed(run: Consumer<Task?>?): Task? {
            return syncDelayed(GeckoLib.get(), run!!)
        }

        /**
         * Schedules a sync delayed task to run after a delay
         *
         * @param run   The task to run
         * @param delay The delay in ticks to wait before running the task
         * @return The Task that has been scheduled
         */
        fun syncDelayed(run: Runnable?, delay: Long): Task? {
            return syncDelayed(GeckoLib.get(), run!!, delay)
        }

        /**
         * Schedules a sync delayed task to run after a delay
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @return The Task that has been scheduled
         */
        /**
         * Schedules a sync delayed task to run as soon as possible
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @return The Task that has been scheduled
         */
        @JvmOverloads
        fun syncDelayed(plugin: Plugin, run: Runnable, delay: Long = 0): Task {
            return syncDelayed(plugin, { t: Task? -> run.run() }, delay)
        }

        /**
         * Schedules a sync delayed task to run after a delay
         *
         * @param run   The task to run
         * @param delay The delay in ticks to wait before running the task
         * @return The Task that has been scheduled
         */
        fun syncDelayed(run: Consumer<Task?>?, delay: Long): Task? {
            return syncDelayed(GeckoLib.get(), run!!, delay)
        }

        /**
         * Schedules a sync delayed task to run after a delay
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @return The Task that has been scheduled
         */
        /**
         * Schedules a sync delayed task to run as soon as possible
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @return The Task that has been scheduled
         */
        @JvmOverloads
        fun syncDelayed(plugin: Plugin, run: Consumer<Task?>, delay: Long = 0): Task {
            val task = arrayOf<Task?>(null)
            task[0] = Task(
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, { run.accept(task[0]) }, delay),
                TaskType.SYNC_DELAYED,
                plugin
            )
            return task[0]!!
        }

        /**
         * Schedules a sync repeating task to run later
         *
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @param period The number of ticks between executions of the task
         * @return The Task that has been scheduled
         */
        fun syncRepeating(run: Runnable?, delay: Long, period: Long): Task? {
            return syncRepeating(GeckoLib.get(), run!!, delay, period)
        }

        /**
         * Schedules a sync repeating task to run later
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @param period The number of ticks between executions of the task
         * @return The Task that has been scheduled
         */
        fun syncRepeating(plugin: Plugin, run: Runnable, delay: Long, period: Long): Task {
            return syncRepeating(plugin, { t: Task? -> run.run() }, delay, period)
        }

        /**
         * Schedules a sync repeating task to run later
         *
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @param period The number of ticks between executions of the task
         * @return The Task that has been scheduled
         */
        fun syncRepeating(run: Consumer<Task?>?, delay: Long, period: Long): Task? {
            return syncRepeating(GeckoLib.get(), run!!, delay, period)
        }

        /**
         * Schedules a sync repeating task to run later
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @param period The number of ticks between executions of the task
         * @return The Task that has been scheduled
         */
        fun syncRepeating(plugin: Plugin, run: Consumer<Task?>, delay: Long, period: Long): Task {
            val task = arrayOf<Task?>(null)
            task[0] = Task(
                Bukkit.getScheduler()
                    .scheduleSyncRepeatingTask(plugin, { run.accept(task[0]) }, delay, period),
                TaskType.SYNC_REPEATING,
                plugin
            )
            return task[0]!!
        }

        /**
         * Schedules an async delayed task to run as soon as possible
         *
         * @param run The task to run
         * @return The Task that has been scheduled
         */
        fun asyncDelayed(run: Runnable?): Task? {
            return asyncDelayed(GeckoLib.get(), run!!)
        }

        /**
         * Schedules an async delayed task to run as soon as possible
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @return The Task that has been scheduled
         */
        fun asyncDelayed(plugin: Plugin, run: Runnable): Task {
            return asyncDelayed(plugin, { t: Task? -> run.run() }, 0)
        }

        /**
         * Schedules an async delayed task to run as soon as possible
         *
         * @param run The task to run
         * @return The Task that has been scheduled
         */
        fun asyncDelayed(run: Consumer<Task?>?): Task? {
            return asyncDelayed(GeckoLib.get(), run!!)
        }

        /**
         * Schedules an async delayed task to run after a delay
         *
         * @param run   The task to run
         * @param delay The delay in ticks to wait before running the task
         * @return The Task that has been scheduled
         */
        fun asyncDelayed(run: Runnable?, delay: Long): Task? {
            return asyncDelayed(GeckoLib.get(), run!!, delay)
        }

        /**
         * Schedules an async delayed task to run after a delay
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @return The Task that has been scheduled
         */
        fun asyncDelayed(plugin: Plugin, run: Runnable, delay: Long): Task {
            return asyncDelayed(plugin, { t: Task? -> run.run() }, delay)
        }

        /**
         * Schedules an async delayed task to run after a delay
         *
         * @param run   The task to run
         * @param delay The delay in ticks to wait before running the task
         * @return The Task that has been scheduled
         */
        fun asyncDelayed(run: Consumer<Task?>?, delay: Long): Task? {
            return asyncDelayed(GeckoLib.get(), run!!, delay)
        }

        /**
         * Schedules an async delayed task to run after a delay
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @return The Task that has been scheduled
         */
        /**
         * Schedules an async delayed task to run as soon as possible
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @return The Task that has been scheduled
         */
        @JvmOverloads
        fun asyncDelayed(plugin: Plugin, run: Consumer<Task?>, delay: Long = 0): Task {
            val task = arrayOf<Task?>(null)
            task[0] = Task(
                Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, { run.accept(task[0]) }, delay),
                TaskType.ASYNC_DELAYED,
                plugin
            )
            return task[0]!!
        }

        /**
         * Schedules an async repeating task to run later
         *
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @param period The number of ticks between executions of the task
         * @return The Task that has been scheduled
         */
        fun asyncRepeating(run: Consumer<Task?>?, delay: Long, period: Long): Task? {
            return asyncRepeating(GeckoLib.get(), run!!, delay, period)
        }

        /**
         * Schedules an async repeating task to run later
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @param period The number of ticks between executions of the task
         * @return The Task that has been scheduled
         */
        fun asyncRepeating(plugin: Plugin, run: Consumer<Task?>, delay: Long, period: Long): Task {
            val task = arrayOf<Task?>(null)
            task[0] = Task(
                Bukkit.getScheduler()
                    .scheduleAsyncRepeatingTask(plugin, { run.accept(task[0]) }, delay, period),
                TaskType.ASYNC_REPEATING,
                plugin
            )
            return task[0]!!
        }

        /**
         * Schedules an async repeating task to run later
         *
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @param period The number of ticks between executions of the task
         * @return The Task that has been scheduled
         */
        fun asyncRepeating(run: Runnable?, delay: Long, period: Long): Task? {
            return asyncRepeating(GeckoLib.get(), run!!, delay, period)
        }

        /**
         * Schedules an async repeating task to run later
         *
         * @param plugin The plugin scheduling the task
         * @param run    The task to run
         * @param delay  The delay in ticks to wait before running the task
         * @param period The number of ticks between executions of the task
         * @return The Task that has been scheduled
         */
        fun asyncRepeating(plugin: Plugin, run: Runnable, delay: Long, period: Long): Task {
            return asyncRepeating(plugin, { t: Task? -> run.run() }, delay, period)
        }
    }
}
